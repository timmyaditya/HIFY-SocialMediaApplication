package database;

import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    //initialize variable
    private List<Todo> todoList;
    private Activity context;
    private RoomDb database;

    //create constructor
    public TodoAdapter(Activity context, List<Todo> todoList)
    {
        this.context = context;
        this.todoList = todoList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        //initialize todo data
        Todo todo = todoList.get(position);
        //initialize database
        database = RoomDb.getInstance(context);
        //set tex on text view
        holder.textView.setText(todo.getText());

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize todo data
                Todo t = todoList.get(holder.getAdapterPosition());
                //get id
                final int sId = t.getId();
                //get text
                final String sText = t.getText();

                //create dialog
                final Dialog dialog = new Dialog(context);
                //set content view
                dialog.setContentView(R.layout.dialog_update);
                //Initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                //initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                //set layout
                dialog.getWindow().setLayout(width, height);
                //show dialog
                dialog.show();

                //initialize and assign variable
                final EditText editText = dialog.findViewById(R.id.edit_text_dialog);
                Button btUpdate = dialog.findViewById(R.id.bt_update);

                //set edit text
                editText.setText(sText);

                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dismiss dialog
                        dialog.dismiss();
                        //get updated text from edit text
                        String uText = editText.getText().toString().trim();

                        //update text in database
                        database.todoDao().update(sId,uText);
                        //notify when data is updated
                        todoList.clear();
                        todoList.addAll(database.todoDao().getAll());
                        notifyDataSetChanged();
                    }
                });
            }
        });


        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize todo data
                Todo t = todoList.get(holder.getAdapterPosition());
                //delete text  from database
                database.todoDao().delete(t);
                //notify whenb data is deleted
                int position = holder.getAdapterPosition();
                todoList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, todoList.size());
            }
        });


    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //initialize variable
        TextView textView;
        ImageView btEdit, btDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //assign variable
            textView = itemView.findViewById(R.id.text_view);
            btEdit = itemView.findViewById(R.id.bt_edit);
            btDelete = itemView.findViewById(R.id.bt_delete);


        }
    }
}
