package com.team25.event.planner.offering.fragments;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
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
        viewModel.ownerId.postValue(this.ownerId);
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
        binding.buttonDownloadPdf.setOnClickListener(v -> downloadReport());
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