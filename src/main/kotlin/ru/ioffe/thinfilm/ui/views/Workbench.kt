package ru.ioffe.thinfilm.ui.views

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.paint.Color
import javafx.stage.FileChooser.ExtensionFilter
import javafx.util.StringConverter
import javafx.util.converter.NumberStringConverter
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.core.model.ExperimentSeries
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.util.Reference
import ru.ioffe.thinfilm.core.util.ExperimentContext
import ru.ioffe.thinfilm.import.Import
import ru.ioffe.thinfilm.ui.Experiment
import ru.ioffe.thinfilm.ui.databinding.LayerModel
import ru.ioffe.thinfilm.ui.views.hooks.ChartHook
import ru.ioffe.thinfilm.ui.views.hooks.TextHook
import tornadofx.*

class Workbench : View() {

    private val layers = mutableListOf<LayerModel>().asObservable()
    private val from = SimpleIntegerProperty(400)
    private val to = SimpleIntegerProperty(1600)
    private val output = SimpleStringProperty()
    private val context = ExperimentContext()
    private val indexes = FXCollections.observableArrayList<Reference<Material>>()
    private val spectrums = FXCollections.observableArrayList<Reference<ExperimentSeries>>()

    init {
        title = "Thin Film Calculator"
        context.materials().subscribe(indexes)
        context.spectrums().subscribe(spectrums)
        layers.add(LayerModel(LayerModel.Ambient, 1.0, 1.0, indexes[0]))
        layers.add(LayerModel(LayerModel.Film, 200.0, 1.0, indexes[1]))
        layers.add(LayerModel(LayerModel.Substrate, 1.0, 1.0, indexes[2]))
    }

    override fun onUndock() {
        super.onUndock()
        context.materials().unsubscribe(indexes)
        context.spectrums().unsubscribe(spectrums)
    }

    override val root = gridpane {
        vgap = 5.0
        style {
            padding = box(5.px)
        }
        lateinit var chart: LineChart<Number, Number>
        row {
            hbox(spacing = 5) {

                button("\uD83D\uDCDA") {
                    action {
                        openInternalWindow(LibraryView(context))
                    }
                    tooltip = tooltip("Open Library")
                }
                button("➕") {
                    action {
                        layers.add(
                            layers.size - 1,
                            LayerModel(LayerModel.Film, 1.0, 1.0, Reference(context.materials(), 0))
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
                    useMaxWidth = true
                    action {
                        if (layers.size > 2) {
                            Experiment(
                                context,
                                layers.map { it.layer(context.materials()) }.toMutableList(),
                                layers.first().layer(context.materials()),
                                layers.last().layer(context.materials()),
                                WavelengthDomain(from.get(), to.get())
                            ).start()
                        } else {
                            println("You have to define at least three layers")
                        }
                    }
                    tooltip = tooltip("Start Experiment")
                }
                button("Import Data") {
                    useMaxWidth = true
                    action {
                        chooseFile(
                            "Choose dataset",
                            arrayOf(ExtensionFilter("oli", "*.oli")),
                            mode = FileChooserMode.Multi
                        ).forEach { file ->
                            alert(
                                Alert.AlertType.INFORMATION,
                                header = "Select data type for file ${file.name}",
                                title = "Select data type",
                                buttons = arrayOf(ButtonType("Transmitted"), ButtonType("Reflected")),
                                actionFn = { alert -> Import(context, alert.text == "Transmitted").apply(file) })
                        }

                    }
                }
            }

        }
        row {
            hbox {
                tableview(layers) {
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
                chart = linechart("System Transmission/Reflection/Absorption", NumberAxis(), NumberAxis()) {
                    createSymbols = false
                }
                context.hooks().add(TextHook(output))
                context.hooks().add(ChartHook(chart))
                listview(spectrums) {
                    style {
                        hAlignment = HPos.RIGHT
                    }
                    cellFormat {
                        graphic = hbox(spacing = 5) {
                            alignment = Pos.BASELINE_LEFT
                            label(item.toString())
                            togglebutton("\uD83D\uDC41") {
                                action {
                                    item.value().enabledProperty.set(isSelected)
                                    context.refresh()
                                }
                            }
                        }
                    }
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