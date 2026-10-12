package com.faceattendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faceattendance.model.Professor;
import com.faceattendance.model.ResponseData;
import com.faceattendance.server.ApiService;
import com.faceattendance.server.RetroClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfessorListAdapter extends RecyclerView.Adapter<ProfessorListAdapter.ViewHolder> {

    private List<Professor> list;
    private Context cnt;

    public ProfessorListAdapter(List<Professor> list, Context cnt) {
        this.list = list;
        this.cnt = cnt;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvEmail;
        TextView tvPhone;
        TextView tvDesignation;

        Button btnActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);

            btnActive = itemView.findViewById(R.id.btnActive);

            tvName.setTextColor(Color.BLACK);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_professor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Professor professor = list.get(position);

        // Name
        holder.tvName.setText(professor.getName());

        // Email
        holder.tvEmail.setText(
                "Email : " + professor.getEmail()
        );

        // Phone
        holder.tvPhone.setText(
                "Phone : " + professor.getPhone()
        );

        // Designation
        holder.tvDesignation.setText(
                professor.getDesignation()
        );

        // Status Button
        if (professor.getSsActive() != null &&
                professor.getSsActive().equalsIgnoreCase("yes")) {

            holder.btnActive.setText("Block");

        } else {

            holder.btnActive.setText("Active");
        }

        holder.btnActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (professor.getSsActive() != null &&
                        professor.getSsActive().equalsIgnoreCase("yes")) {

                    updateStatus(professor.getId(), "no", cnt);

                } else {

                    updateStatus(professor.getId(), "yes", cnt);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    ProgressDialog pd;

    public void updateStatus(String id, String status, Context cnt) {

        pd = new ProgressDialog(cnt);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        ApiService apiService =
                RetroClient.getRetrofitInstance().create(ApiService.class);

        Call<ResponseData> call =
                apiService.updateProfStatus(id, status);

        call.enqueue(new Callback<ResponseData>() {

            @Override
            public void onResponse(Call<ResponseData> call,
                                   Response<ResponseData> response) {

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                Toast.makeText(cnt,
                        "Status Updated",
                        Toast.LENGTH_SHORT).show();

                if (cnt instanceof ProfessorListActivity) {
                    ((ProfessorListActivity) cnt).loadProfessors();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                Toast.makeText(cnt,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}