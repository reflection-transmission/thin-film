package ru.ioffe.thinfilm.ui.views

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TableView
import javafx.scene.paint.Color
import javafx.stage.FileChooser.ExtensionFilter
import javafx.util.converter.NumberStringConverter
import ru.ioffe.thinfilm.core.Experiment
import ru.ioffe.thinfilm.core.math.WavelengthDomain
import ru.ioffe.thinfilm.ui.ExperimentSeries
import ru.ioffe.thinfilm.core.model.LightSource
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.util.Session
import ru.ioffe.thinfilm.core.util.Import
import ru.ioffe.thinfilm.core.util.Reference
import ru.ioffe.thinfilm.core.model.LayerModel
import ru.ioffe.thinfilm.core.persistence.Persistence
import ru.ioffe.thinfilm.ui.views.hooks.ChartHook
import ru.ioffe.thinfilm.ui.views.hooks.TextHook
import tornadofx.*

class Workbench : View() {

    private val from = SimpleIntegerProperty(400)

    private val to = SimpleIntegerProperty(1600)
    private val output = SimpleStringProperty()
    private val source = SimpleObjectProperty<Reference<LightSource>>()
    private val color = SimpleObjectProperty(Color.valueOf("#ffffff"))
    private var context = Session()
    private var layers = context.layers()
    private val indexes = FXCollections.observableArrayList<Reference<Material>>()
    private val spectrums = FXCollections.observableArrayList<Reference<ExperimentSeries>>()
    private val sources = FXCollections.observableArrayList<Reference<LightSource>>()
    private lateinit var chart: LineChart<Number, Number>
    private lateinit var table: TableView<LayerModel>

    init {
        title = "Thin Film Calculator"
        context.materials().subscribe(indexes)
        context.spectrums().subscribe(spectrums)
        context.sources().subscribe(sources)
        source.value = sources[0]
    }

    override fun onUndock() {
        super.onUndock()
        context.materials().unsubscribe(indexes)
        context.spectrums().unsubscribe(spectrums)
    }

    private fun changeSession(new: Session) {
        context.materials().unsubscribe(indexes)
        context.spectrums().unsubscribe(spectrums)
        context.sources().unsubscribe(sources)
        context = new
        indexes.clear()
        spectrums.clear()
        sources.clear()
        context.materials().subscribe(indexes)
        context.spectrums().subscribe(spectrums)
        context.sources().subscribe(sources)
        context.hooks().add(TextHook(output))
        context.hooks().add(ChartHook(chart))
        layers = context.layers()
        table.items = layers
        source.value = sources[0]
        title = new.name()
    }

    override val root = gridpane {
        vgap = 5.0
        style {
            padding = box(5.px)
        }
        row {
            hbox(spacing = 5) {
                button("\uD83D\uDCC2") {
                    action {
                        chooseFile(
                            "Choose session file",
                            arrayOf(ExtensionFilter("Experiment data", "*.exp")),
                            mode = FileChooserMode.Single
                        ).forEach { changeSession(Persistence().load(it)) }
                    }
                }
                button("\uD83D\uDCBE") {
                    action {
                        chooseFile(
                            "Choose a file to save",
                            arrayOf(ExtensionFilter("Experiment data", "*.exp")),
                            mode = FileChooserMode.Save
                        ).forEach { Persistence().save(it, context) }
                    }
                }
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
                            LayerModel(LayerModel.Film, 1.0, Reference(context.materials(), 0))
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
                            val spectrum = Experiment(
                                context,
                                context.sources().get(source.value).applyDomain(WavelengthDomain(from.get(), to.get())),
                                layers.map { it.layer(context.materials()) }.toMutableList()
                            ).start("custom series")
                            color.set(Color.valueOf(ru.ioffe.thinfilm.core.math.Color(spectrum).toRGB()))
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
                combobox(property = source, values = sources) {
                    value = sources[0]
                }
                colorpicker(color) {
                    isMouseTransparent = true
                    opacity = 1.0
                    stylesheet {
                        Stylesheet.arrow {
                            backgroundColor += Color.TRANSPARENT
                        }
                        Stylesheet.arrowButton {
                            backgroundColor += Color.TRANSPARENT
                        }
                    }
                }
            }

        }
        row {
            hbox {
                table = tableview(layers) {
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
                    column("Material", LayerModel::materialProperty) {
                        useComboBox(indexes)
                        remainingWidth()
                    }
                    column(" ", LayerModel::typeProperty).cellFormat {
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
                    maxWidth = Double.MAX_VALUE
                }
                chart = linechart("System Transmission/Reflection/Absorption", NumberAxis(), NumberAxis()) {
                    createSymbols = false
                    animated = false
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
                            togglebutton("\uD83D\uDC41", selectFirst = item.value().enabled) {
                                action {
                                    item.value().enabledProperty.set(isSelected)
                                    context.refresh()
                                }
                            }
                            button("❌") {
                                alignment = Pos.BASELINE_RIGHT
                                style {
                                    backgroundColor += Color.TRANSPARENT
                                }
                                action {
                                    context.spectrums().remove(item.value())
                                    context.refresh()
                                }
                            }
                            togglebutton("T", selectFirst = item.value().transmission) {
                                action {
                                    item.value().transmissionProperty.set(isSelected)
                                    context.refresh()
                                }
                            }
                            togglebutton("R", selectFirst = item.value().reflection) {
                                action {
                                    item.value().reflectionProperty.set(isSelected)
                                    context.refresh()
                                }
                            }
                            togglebutton("A", selectFirst = item.value().absorption) {
                                action {
                                    item.value().absorptionProperty.set(isSelected)
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