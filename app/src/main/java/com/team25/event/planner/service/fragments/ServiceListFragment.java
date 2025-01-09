package com.team25.event.planner.service.fragments;


import static com.team25.event.planner.service.fragments.OwnerHomePage.SERVICE_ID_ARG_NAME;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.team25.event.planner.R;
import com.team25.event.planner.core.listeners.OnDeleteButtonClickListener;
import com.team25.event.planner.core.listeners.OnDetailsClickListener;
import com.team25.event.planner.core.listeners.OnEditButtonClickListener;
import com.team25.event.planner.event.adapters.TopEventsListAdapter;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.event.viewmodel.HomeEventViewModel;

import com.team25.event.planner.offering.dialogs.YesOrNoDialogFragment;
import com.team25.event.planner.service.adapters.ServiceCardsAdapter;
import com.team25.event.planner.service.viewModels.ServiceAddFormViewModel;
import com.team25.event.planner.service.viewModels.ServiceCardsViewModel;

import java.util.HashMap;

public class ServiceListFragment extends ListFragment implements OnEditButtonClickListener, OnDeleteButtonClickListener, OnDetailsClickListener {
    private ServiceCardsAdapter adapter;
    private ServiceCardsViewModel serviceCardsViewModel;
    private NavController navController;
    private ServiceAddFormViewModel mViewModel;
    private boolean filter;
    private String nameFilter;
    private String priceFilter;
    private Boolean availableFilter;
    private Long eventTypeId;
    private Long offeringCategoryId;
    private final String OFFERING_ID = "OFFERING_ID";
    private final String BOOK_SERVICE = "BOOK_SERVICE";

    public ServiceListFragment(ServiceCardsViewModel vm, boolean f, String n, String p, Boolean a,Long eventTypeId, Long offeringCategoryId) {
        this.serviceCardsViewModel = vm;
        this.filter = f;
        this.nameFilter = n;
        this.priceFilter = p;
        this.availableFilter = a;
        this.eventTypeId = eventTypeId;
        this.offeringCategoryId = offeringCategoryId;
    }

    public static ServiceListFragment newInstance(ServiceCardsViewModel vm, boolean f, String n, String p, Boolean a, Long eventTypeId, Long offeringCategoryId){

        return new ServiceListFragment(vm,f,n,p,a, eventTypeId, offeringCategoryId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        serviceCardsViewModel = new ViewModelProvider(requireActivity()).get(ServiceCardsViewModel.class);
        if(filter){
            serviceCardsViewModel.setupFilter(nameFilter, priceFilter, availableFilter, eventTypeId, offeringCategoryId);
        }else{
            serviceCardsViewModel.filterServices();
        }
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph)
        ).get(ServiceAddFormViewModel.class);

        setObserves();
        return inflater.inflate(R.layout.fragment_service_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setObserves();
        if(!filter){
            serviceCardsViewModel.filterServices();
        }

    }

    public void setObserves(){
        serviceCardsViewModel.services.observe(getViewLifecycleOwner(), (serviceCard ->{
            adapter = new ServiceCardsAdapter(requireContext(), serviceCard);
            setListAdapter(adapter);
            adapter.setOnEditButtonClickListener(this);
            adapter.setOnDeleteButtonClickListener(this);
            adapter.setOnDetailsClick(this);
            adapter.notifyDataSetChanged();
        }));
        mViewModel.isDeleted.observe(getViewLifecycleOwner(), deleted ->{
            if(deleted){
                serviceCardsViewModel.getServices(new HashMap<>());
            }
        });

    }
    @Override
    public void onDeleteButtonClick(Long id, String name) {
        YesOrNoDialogFragment dialog = new YesOrNoDialogFragment(new YesOrNoDialogFragment.ConfirmDialogListener() {
            @Override
            public void onConfirm() {
                mViewModel.deleteService(id);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void refresh() {
                serviceCardsViewModel.getServices(new HashMap<>());
            }

        },name);

        dialog.show(getParentFragmentManager(), "ConfirmDialogFragment");

    }

    @Override
    public void onEditButtonClick(Long id, String name) {
        Bundle bundle = new Bundle();
        bundle.putLong(SERVICE_ID_ARG_NAME, id);
        navController.navigate(R.id.action_ownerHomePage_to_serviceAddForm, bundle);
    }

    @Override
    public void onDetailsClick(Long id) {
        Bundle bundle = new Bundle();
        bundle.putLong(OFFERING_ID, id);
        bundle.putLong(EventArgumentNames.ID_ARG, 0L);
        bundle.putBoolean(BOOK_SERVICE,false);
        navController.navigate(R.id.action_ownerHomePage_to_serviceDetailsFragment, bundle);
    }
}