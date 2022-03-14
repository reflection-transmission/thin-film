package ru.ioffe.thinfilm.core.persistence

import ru.ioffe.thinfilm.core.model.Layer
import ru.ioffe.thinfilm.core.model.LayerModel
import ru.ioffe.thinfilm.core.model.Material
import ru.ioffe.thinfilm.core.model.Series
import ru.ioffe.thinfilm.core.util.Session
import ru.ioffe.thinfilm.ui.ExperimentSeries

@kotlinx.serialization.Serializable
data class Dump(
    val name: String,
    val materials: List<Material>,
    val experiments: List<Series>,
    val layers: List<Layer>
) {
    companion object {
        fun from(session: Session): Dump {
            return Dump(
                session.name(),
                session.materials().values().filter { !it.dispersion.constant },
                session.spectrums().values().map(ExperimentSeries::series),
                session.layers().map { it.layer(session.materials()) }
            )
        }
    }

    fun session(): Session {
        val session = Session()
        materials.forEach { session.materials().add(it) }
        experiments.forEach {
            session.spectrums().add(
                ExperimentSeries(
                    it,
                    enabled = false,
                    transmission = false,
                    reflection = false
                )
            )
        }
        session.layers().clear()
        session.layers().addAll(layers.map { LayerModel.create(it, session.materials()) })
        return session
    }
}