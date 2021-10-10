package com.indaco.hue.ui.screens.children

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import com.indaco.hue.databinding.RowItemLightBinding
import com.indaco.samples.data.models.mockhue.Light
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LightAdapter(val viewmodel: HueLightViewModel, val scope: CoroutineScope): RecyclerView.Adapter<LightAdapter.LightVH>() {

    private var list: List<Light> = emptyList()

    private val dispatcher = Dispatchers.Default
    private val mainDispatcher = Dispatchers.Main

    init {
        scope.launch(dispatcher) {
            val list = viewmodel.getLights().first()
            withContext(mainDispatcher) { updateList(list) }
        }
    }

    @UiThread
    fun updateList(list: List<Light>?) {
        this.list = list ?: emptyList()
        notifyDataSetChanged()
    }

    class LightVH(val binding: RowItemLightBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightVH =
        LightVH(RowItemLightBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: LightVH, position: Int) {
        with(holder.binding) {
            val light = list[position]

            title.text = light.name

            val color: Int = light.color

            constraintRoot.setBackgroundColor(color)

            val hsv = FloatArray(3)

            Color.colorToHSV(color, hsv)

            seekBar.progress = hsv[0].toInt()

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(s: SeekBar?, p: Int, fromUser: Boolean) {
                    changeRootColor(constraintRoot, p)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) { }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    light.color = progressToColor(seekBar!!.progress.toFloat())

                    viewmodel.saveLight(light)
                }
            })
        }
    }

    private fun progressToColor(progress: Float): Int =
        Color.HSVToColor(floatArrayOf(progress, 1f, 1f))

    private fun changeRootColor(view: View, progress: Int) =
        view.setBackgroundColor(progressToColor(progress.toFloat()))

    override fun getItemCount(): Int = list.size

}