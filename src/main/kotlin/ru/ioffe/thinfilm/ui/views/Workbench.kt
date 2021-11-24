package ru.ioffe.thinfilm.ui.views

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.paint.Color
import javafx.util.StringConverter
import javafx.util.converter.NumberStringConverter
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.net.MaterialRegistry
import ru.ioffe.thinfilm.ui.Experiment
import ru.ioffe.thinfilm.ui.databinding.LayerModel
import ru.ioffe.thinfilm.ui.databinding.MaterialReference
import tornadofx.*

class Workbench : View() {

    private val layers = mutableListOf<LayerModel>().asObservable()
    private val from = SimpleIntegerProperty(400)
    private val to = SimpleIntegerProperty(1600)
    private val output = SimpleStringProperty()

    private val registry = MaterialRegistry()
    private val indexes = FXCollections.observableArrayList<MaterialReference>()

    init {
        title = "Thin Film Calculator"
        registry.subscribe(indexes)
        layers.add(LayerModel(LayerModel.Ambient, 1.0, 1.0, indexes[0]))
        layers.add(LayerModel(LayerModel.Film, 200.0, 1.0, indexes[1]))
        layers.add(LayerModel(LayerModel.Substrate, 1.0, 1.0, indexes[2]))
    }

    override fun onUndock() {
        super.onUndock()
        registry.subscribe(indexes)
    }

    override val root = gridpane {
        vgap = 5.0
        style {
            padding = box(5.px)
        }
        row {
            lateinit var chart: LineChart<Number, Number>
            vbox {
                hbox(spacing = 5) {
                    style {
                        padding = box(0.px, 0.px, 5.px, 0.px)
                    }
                    gridpaneColumnConstraints {
                        percentWidth = 50.0
                    }
                    button("\uD83D\uDCDA") {
                        action {
                            openInternalWindow(LibraryView(registry))
                        }
                        tooltip = tooltip("Open Library")
                    }
                    button("➕") {
                        action {
                            layers.add(
                                layers.size - 1,
                                LayerModel(LayerModel.Film, 1.0, 1.0, MaterialReference(registry, 0))
                            )
                        }
                        tooltip = tooltip("Add Layer")
                    }
                    textfield(from) {
                        tooltip = tooltip("Lower point of wavelength domain range")
                    }
                    textfield(to) {
                        tooltip = tooltip("Last point of wavelength domain range")
                    }
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
                        tooltip = tooltip("Start Experiment")
                    }
                }
                tableview(layers) {
                    gridpaneColumnConstraints {
                        percentWidth = 50.0
                    }
                    useMaxWidth = true
                    isEditable = true
                    readonlyColumn("Type", LayerModel::typeProperty).cellFormat {
                        alignment = Pos.BASELINE_CENTER
                        text = when (it.value) {
                            0 -> "Film"
                            1 -> "Ambient"
                            2 -> "Substrate"
                            else -> "Undefined"
                        }
                    }
                    column("Depth (nm)", LayerModel::depthProperty) {
                        makeEditable()
                        useTextField(NumberStringConverter())
                    }
                    column("Fulfill", LayerModel::fulfillProperty) {
                        makeEditable()
                        useTextField(object : StringConverter<Number>() {
                            override fun fromString(string: String?): Double = (string ?: "0").toDouble()
                            override fun toString(double: Number?): String = double.toString()
                        })
                    }
                    column("Material", LayerModel::materialProperty) {
                        useComboBox(indexes)
                        remainingWidth()
                    }
                    column(" ", LayerModel::fulfillProperty).cellFormat {
                        val element: LayerModel = this@cellFormat.rowItem
                        if (element.type == 0) {
                            graphic = hbox {
                                useMaxWidth = true
                                button("❌") {
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
            }
            vbox {
                chart = linechart("System Transmission/Reflection/Absorption", NumberAxis(), NumberAxis()) {
                    setCreateSymbols(false)
                }
            }
        }
        row {
            style {
                padding = box(5.px, 0.px, 0.px, 0.px)
            }
            textarea(output) {
                isEditable = false
                gridpaneConstraints {
                    columnSpan = 2
                }
            }
        }
    }
}