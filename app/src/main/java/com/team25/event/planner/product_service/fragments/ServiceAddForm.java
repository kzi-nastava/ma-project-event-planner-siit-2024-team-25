package com.team25.event.planner.product_service.fragments;

import static com.team25.event.planner.offering.fragments.OfferingCategoryBaseFragment.ID_ARG_NAME;
import static com.team25.event.planner.product_service.fragments.OwnerHomePage.SERVICE_ID_ARG_NAME;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team25.event.planner.R;
import com.team25.event.planner.databinding.FragmentServiceAddFormBinding;
import com.team25.event.planner.product_service.viewModels.ServiceAddFormViewModel;

public class ServiceAddForm extends Fragment {
    private ServiceAddFormViewModel mViewModel;
    private NavController navController;
    private FragmentServiceAddFormBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service_add_form, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        mViewModel = new ViewModelProvider(
                NavHostFragment.findNavController(this).getViewModelStoreOwner(R.id.nav_graph)
        ).get(ServiceAddFormViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        if(getArguments() != null){
            TextView textView = binding.EditOrCreateServiceText;
            textView.setText(R.string.edit_the_service);
            mViewModel.isEditMode.setValue(true);
            Long serviceId = getArguments().getLong(SERVICE_ID_ARG_NAME);
            mViewModel.setUpServiceId(serviceId);
        }
        else{
            mViewModel.isEditMode.setValue(false);
            mViewModel.setUpServiceId(null);
        }

        setObservers(getArguments());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setObservers(getArguments());
    }

    public void setObservers(Bundle argumentsBundle){
        mViewModel.firstToSecond.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                if(mViewModel.validateForm()){
                    navController.navigate(R.id.action_serviceAddForm_to_secondPageCreatingServiceFragment, argumentsBundle);

                    mViewModel.firstToSecond.setValue(false);
                }

            }
        });
        mViewModel.cancelClicked.observe(getViewLifecycleOwner(), navigate -> {
            if (navigate != null && navigate) {
                navController.navigateUp();
                mViewModel.cancelClicked.setValue(false);
            }
        });

    }

}