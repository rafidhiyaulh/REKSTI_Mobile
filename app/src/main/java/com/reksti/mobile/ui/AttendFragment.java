package com.reksti.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.reksti.mobile.R;
import com.reksti.mobile.model.AttendRequest;
import com.reksti.mobile.model.AttendResponse;
import com.reksti.mobile.network.ApiClient;
import com.reksti.mobile.network.ApiService;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendFragment extends Fragment {

    private EditText etClassUid;
    private ImageView ivPreview;
    private Button btnCapture, btnSubmit;
    private Bitmap capturedImage;

    private static final int CAMERA_REQUEST_CODE = 102;

    public AttendFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attend, container, false);

        etClassUid = view.findViewById(R.id.et_class_uid);
        ivPreview = view.findViewById(R.id.iv_attend_preview);
        btnCapture = view.findViewById(R.id.btn_capture_attend);
        btnSubmit = view.findViewById(R.id.btn_submit_attend);

        btnCapture.setOnClickListener(v -> openCamera());
        btnSubmit.setOnClickListener(v -> submitAttendance());

        return view;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            capturedImage = (Bitmap) data.getExtras().get("data");
            ivPreview.setImageBitmap(capturedImage);
        }
    }

    private void submitAttendance() {
        String classUid = etClassUid.getText().toString();

        if (classUid.isEmpty() || capturedImage == null) {
            Toast.makeText(getContext(), "Isi class_uid dan ambil foto terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        String base64Image = convertBitmapToBase64(capturedImage);
        AttendRequest request = new AttendRequest(classUid, base64Image);

        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<AttendResponse> call = apiService.attendClass(request);

        call.enqueue(new Callback<AttendResponse>() {
            @Override
            public void onResponse(@NonNull Call<AttendResponse> call,
                                   @NonNull Response<AttendResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    Toast.makeText(getContext(), "Absensi berhasil\nNama: " + response.body().getStudent(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Absensi gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AttendResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
        byte[] byteArray = stream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
}
