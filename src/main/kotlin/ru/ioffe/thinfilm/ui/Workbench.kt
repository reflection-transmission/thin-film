package ru.ioffe.thinfilm.ui

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.chart.NumberAxis
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.net.MaterialRegistry
import ru.ioffe.thinfilm.ui.databinding.FilmLayerModel
import ru.ioffe.thinfilm.ui.databinding.MaterialReference
import tornadofx.*

class Workbench(private val registry: MaterialRegistry) : View() {

    private val layers = mutableListOf<FilmLayerModel>().asObservable()
    private val from = SimpleIntegerProperty(400)
    private val to = SimpleIntegerProperty(1600)
    private val output = SimpleStringProperty()

    private val indexes = FXCollections.observableArrayList<MaterialReference>()

    init {
        registry.subscribe(indexes)
    }

    override val root = gridpane {
        row {
            vbox {
                hbox {
                    button("Append Layer").action {
                        layers.add(FilmLayerModel(1.0, 1.0, MaterialReference(registry, 0)))
                    }
                    button("Tail Layer").action {
                        if (layers.size > 1) layers.removeLast()
                    }
                    textfield(from)
                    textfield(to)
                }
                tableview(layers) {
                    gridpaneColumnConstraints {
                        percentWidth = 50.0
                    }
                    useMaxWidth = true
                    isEditable = true
                    column("depth (nm)", FilmLayerModel::depthProperty).makeEditable()
                    column("fulfill", FilmLayerModel::fulfillProperty).makeEditable()
                    column("material", FilmLayerModel::materialProperty).useComboBox(indexes)
                }
            }
            vbox {
                val chart = linechart("Material n and k", NumberAxis(), NumberAxis()) {
                    setCreateSymbols(false)
                }
                button("Run").action {
                    if (layers.size > 2) {
                        val result = Experiment(
                            layers.map { it.layer(registry) }.toMutableList(),
                            layers.first().layer(registry),
                            layers.last().layer(registry),
                            WavelengthDomain(from.get(), to.get())
                        ).start()
                        result.draw(chart)
                        result.out(output)
                    } else {
                        println("You have to define at least three layers")
                    }
                }
            }
        }
        row {
            textarea(output) {
                isEditable = false
                gridpaneConstraints {
                    columnSpan = 2
                }
            }
        }
    }
}