package com.team25.event.planner.offering.fragments;


import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.app.DownloadManager;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.team25.event.planner.R;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.core.listeners.OnEditPriceListClickListener;
import com.team25.event.planner.databinding.FragmentProductPriceListBinding;
import com.team25.event.planner.offering.adapters.ProductPriceListAdapter;
import com.team25.event.planner.offering.viewmodel.PriceListViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;


public class ProductPriceListFragment extends Fragment implements OnEditPriceListClickListener {
    public static final String ID_OFFERING_ARG_NAME = "offeringId";
    public static final String PRICE_ARG_NAME = "offeringPrice";
    public static final String DISCOUNT_ARG_NAME = "offeringDiscount";
    private FragmentProductPriceListBinding binding;
    private NavController navController;
    private PriceListViewModel viewModel;
    private ProductPriceListAdapter adapter;
    private ListView listView;
    private boolean isProductsView = false;
    private Long ownerId;

    public ProductPriceListFragment(boolean flag, Long id) {
        isProductsView = flag;
        ownerId = id;

    }

    public static ProductPriceListFragment newInstance(boolean flag, Long id){
        return new ProductPriceListFragment(flag, id);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductPriceListBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        viewModel = new ViewModelProvider(this).get(PriceListViewModel.class);
        viewModel.ownerId.setValue(this.ownerId);
        binding.setViewModel(viewModel);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = binding.list;

        setUpObservers();
        setUpListeners();
        if(isProductsView){
            viewModel.fetchProductsPriceList();
        }else{
            viewModel.fetchServicesPriceList();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpObservers();
        setUpListeners();
        if(isProductsView){
            viewModel.fetchProductsPriceList();
        }else{
            viewModel.fetchServicesPriceList();
        }
    }

    private void setUpListeners() {

        binding.buttonDownloadPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.generatePDF(isProductsView);
            }
        });

        //binding.buttonDownloadPdf.setOnClickListener(v -> downloadReport());

    }

    private void setUpObservers() {
        viewModel.priceListItems.observe(getViewLifecycleOwner(), res ->{
            adapter = new ProductPriceListAdapter(requireContext(), res);
            adapter.setListener(this);
            listView.setAdapter(adapter);
        });
        viewModel.serverError.observe(getViewLifecycleOwner(), mess->{
            if(mess != null){
                Toast.makeText(requireContext(), mess, Toast.LENGTH_SHORT).show();
                viewModel.clearError();
            }
        });
        viewModel.pdfData.observe(getViewLifecycleOwner(), responseBody -> {
            if(responseBody != null) {
                saveFile(responseBody, requireContext());
            }
        });
    }

    @Override
    public void onEditButtonClick(Long id, Double price, Double discount) {
        Bundle bundle = new Bundle();
        bundle.putLong(ID_OFFERING_ARG_NAME, id);
        bundle.putDouble(PRICE_ARG_NAME, price);
        bundle.putDouble(DISCOUNT_ARG_NAME, discount);
        navController.navigate(R.id.action_priceListPage_to_editPriceListItemFragment, bundle);
    }


    public void saveFile(ResponseBody body, Context context) {
        ContentResolver resolver = context.getContentResolver();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "price-list-report");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

        try (OutputStream outputStream = resolver.openOutputStream(uri);
             InputStream inputStream = body.byteStream()) {

            byte[] buffer = new byte[4096];
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();

            Toast.makeText(context, "File saved to Downloads: " , Toast.LENGTH_LONG).show();

            // Otvori fajl odmah ako želiš (intent)
            openPdfFile(context, uri);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save file", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPdfFile(Context context, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No app found to open PDF", Toast.LENGTH_SHORT).show();
        }
    }



    private void downloadReport() {
        final Long ownerId = viewModel.ownerId.getValue();
        if (ownerId == null) {
            Toast.makeText(getContext(), "Unable to download report", Toast.LENGTH_SHORT).show();
            return;
        }
        if(viewModel.priceListItems.getValue().isEmpty()){
            Toast.makeText(getContext(), "There is no offering items", Toast.LENGTH_SHORT).show();
            return;
        }

        final String url = ConnectionParams.BASE_URL + "api/price-list/" + ownerId + "/price-list-report";
        final String filename = "price_list_" + ownerId + "_" + System.currentTimeMillis() + ".pdf";

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Price list report");
        request.setDescription("Downloading PDF report with price list...");
        if(isProductsView){
            request.addRequestHeader("isProductList", "true");
        }else{
            request.addRequestHeader("isProductList", "false");
        }
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        DownloadManager downloadManager = (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(requireContext(), "Download started!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Unable to download report", Toast.LENGTH_SHORT).show();
        }
    }

}