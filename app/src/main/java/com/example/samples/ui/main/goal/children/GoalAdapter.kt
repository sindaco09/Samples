package com.example.samples.ui.main.goal.children

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.samples.data.models.goal.Goal
import com.example.samples.data.models.goal.GoalStatus
import com.example.samples.data.models.goal.IndexedGoal
import com.example.samples.databinding.RowItemDraggableBinding
import com.example.samples.ui.main.goal.GoalViewModel

class GoalAdapter(var list: MutableList<Goal>): RecyclerView.Adapter<GoalAdapter.DragAndDropVH>() {

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
                MotionEvent.ACTION_DOWN -> startDragEvent(v)
                MotionEvent.ACTION_UP -> v?.performClick()
                else -> { }
            }
            return true
        }

        private fun startDragEvent(v: View?) {
            val item = ClipData.Item(
                Intent().putExtra(IndexedGoal.GOAL, IndexedGoal(position, data)))

            val description = ClipDescription("", arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN))

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

    class AdapterDragAndDropListener(val goalStatus: GoalStatus, val viewmodel: GoalViewModel): View.OnDragListener {
        override fun onDrag(v: View?, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // when event is dropped, the drag ended!

                    val rowItem = getRowItem(event.clipData)

                    viewmodel.updateGoal(rowItem.goal.apply { status = goalStatus })

                    // use below to update goal via actual drop, otherwise in updateGoal
                    // remove call to getGoals()

//                    val child: View = event.localState as View
//                    val oldParent: RecyclerView = child.parent as RecyclerView
//                    val adapter = oldParent.adapter as GoalAdapter
//                    adapter.removeItemAt(rowItem.position)
//
//                    val newParent: RecyclerView = v!! as RecyclerView
//                    val newAdapter = newParent.adapter as GoalAdapter
//                    newAdapter.insertItem(rowItem.goal)
                }
            }

            return true
        }

        companion object {
            fun getRowItem(clipData: ClipData): IndexedGoal =
                clipData.getItemAt(0).intent.getSerializableExtra(IndexedGoal.GOAL)!! as IndexedGoal
        }
    }


    fun removeItemAt(position: Int) {
        Log.d("TAG","removeItemAt $position")
        list.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, list.size)
    }

    fun insertItem(item: Goal) {
        Log.d("TAG","insertItem ${item.id}")
        list.add(0, item)
        notifyItemInserted(0)
        notifyItemRangeChanged(0, list.size)
    }

    fun updateList(list: List<Goal>) {
        Log.d("TAG","updateList ${list.size}")
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }
}