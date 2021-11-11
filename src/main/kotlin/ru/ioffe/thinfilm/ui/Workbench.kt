package ru.ioffe.thinfilm.ui

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.net.MaterialRegistry
import ru.ioffe.thinfilm.ui.databinding.LayerModel
import ru.ioffe.thinfilm.ui.databinding.MaterialReference
import tornadofx.*

class Workbench(private val registry: MaterialRegistry) : View() {

    private val layers = mutableListOf<LayerModel>().asObservable()
    private val from = SimpleIntegerProperty(400)
    private val to = SimpleIntegerProperty(1600)
    private val output = SimpleStringProperty()

    private val indexes = FXCollections.observableArrayList<MaterialReference>()

    init {
        registry.subscribe(indexes)
        layers.add(LayerModel(LayerModel.Ambient, 1.0, 1.0, indexes[0]))
        layers.add(LayerModel(LayerModel.Film, 200.0, 1.0, indexes[1]))
        layers.add(LayerModel(LayerModel.Substrate, 1.0, 1.0, indexes[2]))
    }

    override val root = gridpane {
        row {
            lateinit var chart: LineChart<Number, Number>
            vbox {
                hbox {
                    gridpaneColumnConstraints {
                        percentWidth = 50.0
                    }
                    textfield(from)
                    textfield(to)
                    button("Run") {
                        useMaxSize = true
                        useMaxWidth = true
                        action {
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
                tableview(layers) {
                    gridpaneColumnConstraints {
                        percentWidth = 50.0
                    }
                    useMaxWidth = true
                    isEditable = true
                    readonlyColumn("Type", LayerModel::typeProperty).cellFormat {
                        text = when (it.value) {
                            0 -> "Film"
                            1 -> "Ambient"
                            2 -> "Substrate"
                            else -> "Undefined"
                        }
                    }
                    column("Depth (nm)", LayerModel::depthProperty).makeEditable()
                    column("Fulfill", LayerModel::fulfillProperty).makeEditable()
                    column("Material", LayerModel::materialProperty) {
                        useComboBox(indexes)
                        remainingWidth()
                    }
                    column(" ", LayerModel::fulfillProperty).cellFormat {
                        val element: LayerModel = this@cellFormat.rowItem
                        if (element.type == 0) {
                            graphic = hbox {
                                useMaxWidth = true
                                button("remove") {
                                    style {
                                        backgroundColor += Color.TRANSPARENT
                                    }
                                    action {
                                        if (layers.size > 3) {
                                            layers.remove(element)
                                        }
                                    }

                                }
                            }
                        }
                    }
                    smartResize()
                }
                hbox {
                    button(" + ").action {
                        layers.add(
                            layers.size - 1,
                            LayerModel(LayerModel.Film, 1.0, 1.0, MaterialReference(registry, 0))
                        )
                    }
                }
            }
            vbox {
                chart = linechart("Material n and k", NumberAxis(), NumberAxis()) {
                    setCreateSymbols(false)
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