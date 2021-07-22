package com.example.stop_and_flight;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stop_and_flight.model.Ticket;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TaskAdapter adapter;
    private TicketAdapter ticketadapter;

    public RecyclerItemTouchHelper(TaskAdapter adapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    public RecyclerItemTouchHelper(TicketAdapter ticketadapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.ticketadapter = ticketadapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction){
        final int position = viewHolder.getAdapterPosition();
        if (adapter != null)
        {
            if(direction == ItemTouchHelper.LEFT) {
                adapter.deleteItem(position);
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
            else {
                adapter.editItem(position);
                adapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }
        else if (ticketadapter != null)
        {
            if(direction == ItemTouchHelper.LEFT) {
                ticketadapter.deleteItem(position);
                ticketadapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
            else {
                ticketadapter.editItem(position);
                ticketadapter.notifyItemChanged(viewHolder.getAdapterPosition());
            }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive)
    {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon = null;
        ColorDrawable background = null;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset =20;

        if (adapter != null) {
            if (dX > 0) {
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);
            } else {
                icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
            }
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), R.color.colorPrimaryDark));
        }
        else if (ticketadapter != null) {
            if (dX > 0) {
                icon = ContextCompat.getDrawable(ticketadapter.getContext(), R.drawable.ic_baseline_edit);
            } else {
                icon = ContextCompat.getDrawable(ticketadapter.getContext(), R.drawable.ic_baseline_delete);
            }
            background = new ColorDrawable(ContextCompat.getColor(ticketadapter.getContext(), R.color.colorPrimaryDark));
        }
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();
        if (dX > 0){
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();

            icon.setBounds(iconLeft, iconTop,iconRight ,iconBottom);
            background.setBounds(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + ((int)dX) + backgroundCornerOffset, itemView.getBottom());
        }
        else if (dX < 0) {
            int iconLeft = itemView.getRight() -  iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;

            icon.setBounds(iconLeft, iconTop,iconRight ,iconBottom);
            background.setBounds(itemView.getRight() + ((int)dX) - backgroundCornerOffset, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        }
        else {
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
