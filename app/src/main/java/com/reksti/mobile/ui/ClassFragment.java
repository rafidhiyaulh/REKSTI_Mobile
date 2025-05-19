// ClassFragment.java
package com.reksti.mobile.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reksti.mobile.R;
import com.reksti.mobile.model.ClassInfoResponse;
import com.reksti.mobile.network.ApiClient;
import com.reksti.mobile.network.ApiService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClassFragment extends Fragment {

    private EditText etClassUid;
    private TextView tvResult;
    private Button btnFetch;

    public ClassFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_info, container, false);

        etClassUid = view.findViewById(R.id.et_class_uid);
        tvResult = view.findViewById(R.id.tv_class_info);
        btnFetch = view.findViewById(R.id.btn_fetch_class);

        btnFetch.setOnClickListener(v -> fetchClassInfo());

        return view;
    }

    private void fetchClassInfo() {
        String classUid = etClassUid.getText().toString().trim();
        if (classUid.isEmpty()) {
            Toast.makeText(getContext(), "Class UID harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<ClassInfoResponse> call = apiService.getClass(classUid);

        call.enqueue(new Callback<ClassInfoResponse>() {
            @Override
            public void onResponse(@NonNull Call<ClassInfoResponse> call, @NonNull Response<ClassInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StringBuilder builder = new StringBuilder();
                    for (Map<String, Object> student : response.body().getStudents()) {
                        builder.append(student.toString()).append("\n\n");
                    }
                    tvResult.setText(builder.toString());
                } else {
                    Toast.makeText(getContext(), "Gagal mendapatkan data kelas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ClassInfoResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
