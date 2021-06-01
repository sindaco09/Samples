package com.example.samples.ui.main.goal

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.samples.data.models.goal.Goal
import com.example.samples.databinding.RowItemDraggableBinding
import kotlinx.parcelize.Parcelize

class GoalAdapter(var list: MutableList<Goal>):
    RecyclerView.Adapter<GoalAdapter.DragAndDropVH>()
{
    companion object {
        private const val TAG = "DragAdapter"
    }
    class DragAndDropVH(val binding: RowItemDraggableBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DragAndDropVH {
        return DragAndDropVH(RowItemDraggableBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: DragAndDropVH, i: Int) {
        val item = list[i]
        with(holder.binding) {
            cardTitle.text = item.todoItem
            card.setOnTouchListener(CardClickListener(i, item))
        }
    }

    override fun getItemCount(): Int = list.size

    class CardClickListener(val position: Int, val data: Goal): View.OnTouchListener {

        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d(TAG,"action down")
                    startDragEvent(v)
                }
                MotionEvent.ACTION_UP -> v?.performClick()
                else -> {
                    Log.d(TAG,"action else")
                }
            }
            return true
        }

        private fun startDragEvent(v: View?) {
            val item = ClipData.Item(Intent()
                .putExtra("goal", AdapterDragAndDropListener.RowItem(position, data))
            )
            val description = ClipDescription("Goal", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN))

            val data = ClipData(description, item)

            // Starts the drag
            v?.startDragAndDrop(
                data,           // the data to be dragged
                View.DragShadowBuilder(v),  // the drag shadow builder
                v,              // set view as dragged item vs shadowBuilder, otherwise set null
                0          // flags (not currently used, set to 0)
            )
        }

    }

    class AdapterDragAndDropListener: View.OnDragListener {
        override fun onDrag(v: View?, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // when event is dropped, the drag ended!

                    val rowItem = getRowItem(event.clipData)

                    Log.d(TAG,"Drag Drop: data: ${rowItem.goal.todoItem}, position: ${rowItem.position}")

                    val child: View = event.localState as View
                    val oldParent: RecyclerView = child.parent as RecyclerView
                    val adapter = oldParent.adapter as GoalAdapter
                    adapter.removeItemAt(rowItem.position)

                    val newParent: RecyclerView = v!! as RecyclerView
                    val newAdapter = newParent.adapter as GoalAdapter
                    newAdapter.insertItem(rowItem.goal)
                }
            }

            return true
        }

        private fun getRowItem(clipData: ClipData): RowItem {
            val clipItem = clipData.getItemAt(0)
            return clipItem.intent.getParcelableExtra<RowItem>("goal") as RowItem
        }

        @Parcelize
        class RowItem(val position: Int, val goal: Goal): Parcelable
    }


    fun removeItemAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    fun insertItem(item: Goal) {
        list.add(0, item)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, list.size)
    }
}