package com.reksti.mobile.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reksti.mobile.R;
import com.reksti.mobile.model.Student;
import com.reksti.mobile.network.ApiService;
import com.reksti.mobile.network.RetrofitClient;
import com.reksti.mobile.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private TextView tvName, tvLog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvLog = view.findViewById(R.id.tv_log);

        fetchStudentInfo();

        return view;
    }

    private void fetchStudentInfo() {
        String baseUrl = SharedPrefManager.getBaseUrl(requireContext());

        ApiService apiService = RetrofitClient.getClient(baseUrl).create(ApiService.class);

        Call<Student> call = apiService.getStudent();
        call.enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Student student = response.body();
                    tvName.setText("Nama: " + student.getName());
                    tvLog.setText("Jumlah kelas: " + student.getClasses().size());
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                tvName.setText("Gagal memuat data");
                tvLog.setText(t.getMessage());
            }
        });
    }
}
