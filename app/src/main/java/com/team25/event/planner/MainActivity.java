package com.team25.event.planner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.navigation.NavigationView;
import com.team25.event.planner.communication.model.Notification;
import com.team25.event.planner.communication.model.NotificationCategory;
import com.team25.event.planner.communication.viewmodel.MyNotificationViewModel;
import com.team25.event.planner.communication.viewmodel.NotificationViewModel;
import com.team25.event.planner.core.ConnectionParams;
import com.team25.event.planner.core.SharedPrefService;
import com.team25.event.planner.core.viewmodel.AuthViewModel;
import com.team25.event.planner.databinding.ActivityMainBinding;
import com.team25.event.planner.event.fragments.EventArgumentNames;
import com.team25.event.planner.product.fragments.ProductFormFragment;
import com.team25.event.planner.user.model.UserRole;


public class MainActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityMainBinding binding;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private AppBarConfiguration appBarConfiguration;
    private AuthViewModel authViewModel;
    private NotificationViewModel _notificationViewModel;

    private MyNotificationViewModel _myNotificationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SplashScreen.installSplashScreen(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setSupportActionBar(binding.topAppBar);

        drawer = binding.getRoot();
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    if (navController.getCurrentDestination() != null
                            && navController.getCurrentDestination().getId() == R.id.homeFragment) {
                        finish();
                    } else {
                        navController.popBackStack();
                    }
                }
            }
        });

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        SharedPrefService sharedPrefService = new SharedPrefService(this);
        authViewModel.initialize(sharedPrefService);

        _notificationViewModel = new NotificationViewModel(this);
        _myNotificationViewModel = new MyNotificationViewModel();

        setupNavigationView();
        setupAuthInterceptor();
        setupObserver();
    }

    private void setupObserver(){
        authViewModel.notification.observeForever(notification -> {
            Log.d("AuthViewModel", "Notification value changed: " + notification);
            if(notification){
                this.openWebSocket();
            } else if (!notification) {
                this.closeWebSocket();
            }
        });
    }

    public void openWebSocket() {
        this.authViewModel.user.observe(this, user -> {
            if (user != null) {
                _notificationViewModel.connectToSocket(user);
            }
        });
    }

    public void closeWebSocket() {
        this.authViewModel.user.observe(this, user -> {
            if (user != null) {
                _notificationViewModel.disconnect();
            }
        });
    }

    private void handleIntent(Intent intent) {
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();

            if (data != null) {
                String path = data.getPath();
                if (path != null && path.startsWith("/user/register/quick")) {
                    String invitationCode = data.getQueryParameter("invitationCode");

                    Bundle bundle = new Bundle();
                    bundle.putString("invitationCode", invitationCode);

                    navController.navigate(R.id.registerQuickFragment, bundle);
                } else if (path != null && path.startsWith("/event/")) {
                    Long eventId = Long.valueOf(data.getPathSegments().get(1));
                    String invitationCode = data.getQueryParameter("invitationCode");

                    Bundle bundle = new Bundle();
                    bundle.putString("invitationCode", invitationCode);
                    bundle.putLong("eventId", eventId);

                    navController.navigate(R.id.loginFragment, bundle);
                }
            }
        } else if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                Notification notification = (Notification) data.get("notification");
                if (notification == null) {
                    return;
                }
                notification.setIsViewed(true);
                _myNotificationViewModel.updateNotification(notification);

                NotificationCategory notificationCategory = notification.getNotificationCategory();
                UserRole userRole = (UserRole) data.get("user_role");
                Bundle bundle = new Bundle();
                if (notificationCategory == NotificationCategory.OFFERING_CATEGORY) {
                    if (userRole == UserRole.ADMINISTRATOR) {
                        navController.navigate(R.id.offeringCategoryFragment);
                    } else {
                        if(notification.getTitle().contains("Product")){
                            navController.navigate(R.id.myProductsFragment);
                        } else if (notification.getTitle().contains("Service")) {
                            navController.navigate(R.id.ownerHomePage);
                        }
                    }
                } else if (notificationCategory == NotificationCategory.EVENT) {
                    bundle.putLong(EventArgumentNames.ID_ARG, notification.getEntityId());
                    navController.navigate(R.id.eventDetailsFragment, bundle);
                } else if (notificationCategory == NotificationCategory.PRODUCT) {
                    bundle.putLong(ProductFormFragment.ID_ARG_NAME, notification.getEntityId());
                    navController.navigate(R.id.productDetailsFragment, bundle);
                } else if (notificationCategory == NotificationCategory.SERVICE) {
                    bundle.putLong("OFFERING_ID", notification.getEntityId());
                    navController.navigate(R.id.serviceDetailsFragment, bundle);
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            int id = navDestination.getId();

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            if (id == R.id.homeFragment) {
                // Show drawer toggle only on the home fragment
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                // Show back button on other fragments
                actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                // Set a click listener on the navigation icon to handle the back action
                actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
                    if (MainActivity.this.navController.getCurrentBackStackEntry() != null) {
                        MainActivity.this.navController.popBackStack();
                    }
                });
            }
        });

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment).setOpenableLayout(drawer).build();

        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        this.handleIntent(getIntent());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    private void setupNavigationView() {
        authViewModel.user.observe(this, user -> {
            NavigationView navView = binding.navView;
            navView.getMenu().clear();

            View headerView = navView.getHeaderView(0);
            TextView headerTitle = headerView.findViewById(R.id.header_title);
            TextView headerSubtitle = headerView.findViewById(R.id.header_subtitle);
            ImageView headerImage = headerView.findViewById(R.id.header_image);

            if (user == null || user.getUserRole() == null) {
                navView.inflateMenu(R.menu.unauthenticated_nav_menu);

                headerTitle.setText(getString(R.string.navbar_header_title));
                headerSubtitle.setText(getString(R.string.nav_subtitle_default));

                Glide.with(this)
                        .load(R.drawable.ic_person)
                        .circleCrop()
                        .into(headerImage);
            } else {
                switch (user.getUserRole()) {
                    case REGULAR:
                        navView.inflateMenu(R.menu.regular_nav_menu);
                        break;
                    case EVENT_ORGANIZER:
                        navView.inflateMenu(R.menu.event_organizer_nav_menu);
                        break;
                    case OWNER:
                        navView.inflateMenu(R.menu.owner_nav_menu);
                        break;
                    case ADMINISTRATOR:
                        navView.inflateMenu(R.menu.admin_nav_menu);
                        break;
                }

                headerTitle.setText(user.getName());
                headerSubtitle.setText(user.getEmail());

                final String profilePicUrl = ConnectionParams.BASE_URL + "api/users/" + user.getId() + "/profile-picture";
                Glide.with(this)
                        .load(profilePicUrl)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .circleCrop()
                        .into(headerImage);
            }
        });
    }

    private void setupAuthInterceptor() {
        authViewModel.jwt.observe(this, jwt -> {
            ConnectionParams.setup(jwt, this::handleLogout);
            authViewModel.setInterceptorAdded();
        });
    }

    private void handleLogout() {
        runOnUiThread(() -> {
            authViewModel.clearUser();
            authViewModel.clearJwt();

            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();

            navController.popBackStack(R.id.homeFragment, false);
            navController.navigate(R.id.loginFragment);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}