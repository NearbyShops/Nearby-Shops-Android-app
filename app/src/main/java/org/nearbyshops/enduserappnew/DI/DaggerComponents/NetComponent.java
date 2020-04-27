package org.nearbyshops.enduserappnew.DI.DaggerComponents;


import dagger.Component;

import org.nearbyshops.enduserappnew.DI.DaggerModules.AppModule;
import org.nearbyshops.enduserappnew.DI.DaggerModules.NetModule;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.enduserappnew.HomeBackup;
import org.nearbyshops.enduserappnew.Lists.UsersList.Dialogs.AddUserToStaffDialog;
import org.nearbyshops.enduserappnew.Login.LoginGlobalUsingPasswordFragment;
import org.nearbyshops.enduserappnew.Login.LoginLocalUsingOTPFragmentNew;
import org.nearbyshops.enduserappnew.UtilityScreens.PlacePickerGoogleMaps.PlacePickerWithMapFragment;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderCartItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.BackupViewHolderMarketSmall1Mar20;
import org.nearbyshops.enduserappnew.Lists.OrderHistoryPaging.OrdersDataSource;
import org.nearbyshops.enduserappnew.Lists.OrderHistoryPaging.OrdersListPagingFragment;
import org.nearbyshops.enduserappnew.Lists.UsersList.Dialogs.AddUserToShopStaffDialog;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShopStaffPermissions.EditShopStaffPermissionsFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditStaffPermissions.EditStaffPermissionsFragment;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.BackupViewHolderMarket;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.BackupViewHolderMarketSmall;
import org.nearbyshops.enduserappnew.Login.LoginGlobalUsingOTPFragment;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItem;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItemBackup;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderShopItemButton;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelMarkets;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelShop;
import org.nearbyshops.enduserappnew.aSDSAdminModule.DashboardAdmin.SDSAdminDashboardFragment;
import org.nearbyshops.enduserappnew.aSDSAdminModule.MarketsList.MarketsDataSource;
import org.nearbyshops.enduserappnew.aSDSAdminModule.MarketsList.MarketsListFragment;
import org.nearbyshops.enduserappnew.aSellerModule.FilterDeliveryGuy.FilterDeliveryFragment;
import org.nearbyshops.enduserappnew.aSellerModule.InventoryDeliveryPerson.Fragment.DeliveryInventoryFragment;
import org.nearbyshops.enduserappnew.ViewModels.ViewModelUser;
import org.nearbyshops.enduserappnew.adminModule.AddCredit.FragmentAddCredit;
import org.nearbyshops.enduserappnew.adminModule.DashboardAdmin.AdminDashboardFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditServiceConfig.EditConfigurationFragment;
import org.nearbyshops.enduserappnew.Lists.CartItemList.CartItemListFragment;
import org.nearbyshops.enduserappnew.Lists.CartsList.CartsListFragment;
import org.nearbyshops.enduserappnew.Checkout.PlaceOrderActivity;
import org.nearbyshops.enduserappnew.Lists.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.EditDataScreens.EditDeliveryAddress.EditAddressFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItem.EditItemFragmentNew;
import org.nearbyshops.enduserappnew.EditDataScreens.EditItemImage.EditItemImageFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangeEmail.FragmentChangeEmail;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangeEmail.FragmentVerifyEmail;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangePassword.FragmentChangePassword;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangePhone.FragmentChangePhone;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.ChangePhone.FragmentVerifyPhone;
import org.nearbyshops.enduserappnew.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.enduserappnew.EditDataScreens.EditShopImage.EditShopImageFragment;
import org.nearbyshops.enduserappnew.Home;
import org.nearbyshops.enduserappnew.DetailScreens.DetailItem.ItemDetailFragment;
import org.nearbyshops.enduserappnew.ImageList.ImageListForItem.ItemImageListFragment;
import org.nearbyshops.enduserappnew.Lists.ItemsByCategory.ItemsByCatFragment;
import org.nearbyshops.enduserappnew.aSellerModule.ItemsDatabase.ItemsDatabaseFragment;
import org.nearbyshops.enduserappnew.aSellerModule.ItemsInShopByCatSeller.ItemsInShopByCatSellerFragment;
import org.nearbyshops.enduserappnew.aSellerModule.ItemsInShopSeller.ItemsInShopFragment;
import org.nearbyshops.enduserappnew.aSellerModule.InventoryOrders.Fragment.OrdersInventoryFragment;
import org.nearbyshops.enduserappnew.aSellerModule.QuickStockEditor.FragmentShopItem;
import org.nearbyshops.enduserappnew.aSellerModule.DashboardShopAdmin.ShopAdminHomeFragment;
import org.nearbyshops.enduserappnew.Lists.UsersList.UsersListFragment;
import org.nearbyshops.enduserappnew.Lists.TransactionHistory.TransactionFragment;
import org.nearbyshops.enduserappnew.aSellerModule.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.enduserappnew.Lists.ItemsInShopByCategory.ItemsInShopByCatFragment;
import org.nearbyshops.enduserappnew.Login.LoginLocalUsingPasswordFragment;
import org.nearbyshops.enduserappnew.Login.Backups.LoginLocalUsingOTPFragment;
import org.nearbyshops.enduserappnew.Login.ServiceIndicatorFragment;
import org.nearbyshops.enduserappnew.DetailScreens.DetailMarket.MarketDetailFragment;
import org.nearbyshops.enduserappnew.DetailScreens.DetailMarket.RateReviewDialogMarket;
import org.nearbyshops.enduserappnew.Lists.Markets.MarketsFragment;
import org.nearbyshops.enduserappnew.Lists.Markets.MarketsFragmentNew;
import org.nearbyshops.enduserappnew.Lists.Markets.SubmitURLDialog;
import org.nearbyshops.enduserappnew.Lists.Markets.AdapterMarkets;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.ViewHolderMarket;
import org.nearbyshops.enduserappnew.ViewHolders.ViewHolderMarket.ViewHolderMarketSmall;
import org.nearbyshops.enduserappnew.PushOneSignal.UpdateOneSignalID;
import org.nearbyshops.enduserappnew.DetailScreens.DetailOrder.FragmentOrderDetail;
import org.nearbyshops.enduserappnew.Lists.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.enduserappnew.ProfileFragment;
import org.nearbyshops.enduserappnew.Services.UpdateServiceConfiguration;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.RateReviewDialog;
import org.nearbyshops.enduserappnew.DetailScreens.DetailShop.ShopDetailFragment;
import org.nearbyshops.enduserappnew.ImageList.ImageListForShop.ShopImageListFragment;
import org.nearbyshops.enduserappnew.Lists.ShopsAvailableForItem.Backup.AdapterBackup;
import org.nearbyshops.enduserappnew.Lists.ShopsAvailableForItem.ShopItemFragment;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviewAdapter;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviewStats;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviews;
import org.nearbyshops.enduserappnew.Lists.ShopsList.FragmentShopsList;
import org.nearbyshops.enduserappnew.Login.SignUp.ForgotPassword.FragmentCheckResetCode;
import org.nearbyshops.enduserappnew.Login.SignUp.ForgotPassword.FragmentEnterCredentials;
import org.nearbyshops.enduserappnew.Login.SignUp.ForgotPassword.FragmentResetPassword;
import org.nearbyshops.enduserappnew.Login.SignUp.FragmentEmailOrPhone;
import org.nearbyshops.enduserappnew.Login.SignUp.FragmentEnterPassword;
import org.nearbyshops.enduserappnew.Login.SignUp.FragmentVerify;
import org.nearbyshops.enduserappnew.EditDataScreens.Deprecated.EditShopForAdmin.EditShopForAdminFragment;
import org.nearbyshops.enduserappnew.adminModule.ItemsDatabaseForAdmin.ItemsDatabaseForAdminFragment;
import org.nearbyshops.enduserappnew.adminModule.ChangeParent.Adapter;
import org.nearbyshops.enduserappnew.adminModule.ChangeParent.ItemCategoriesParent;
import org.nearbyshops.enduserappnew.adminModule.ShopsList.Fragment.FragmentShopList;
import org.nearbyshops.enduserappnew.adminModule.DashboardStaff.StaffDashboardFragment;


import javax.inject.Singleton;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {


    void Inject(UpdateOneSignalID updateOneSignalID);

    void Inject(org.nearbyshops.enduserappnew.Lists.ShopsAvailableForItem.Adapter adapter);

    void Inject(ShopItemFragment shopItemFragment);

    void Inject(Home home);

    void Inject(UpdateServiceConfiguration updateServiceConfiguration);

    void Inject(ItemDetailFragment itemDetailFragment);

    void Inject(ItemImageListFragment itemImageListFragment);

    void Inject(LoginGlobalUsingPasswordFragment loginGlobalUsingPasswordFragment);

    void Inject(LoginLocalUsingOTPFragment loginLocalUsingOTPFragment);

    void Inject(ServiceIndicatorFragment serviceIndicatorFragment);

    void Inject(LoginLocalUsingPasswordFragment loginLocalUsingPasswordFragment);

    void Inject(OrdersHistoryFragment ordersHistoryFragment);

    void Inject(FragmentOrderDetail fragmentOrderDetail);

    void Inject(ShopDetailFragment shopDetailFragment);

    void Inject(ShopImageListFragment shopImageListFragment);

    void Inject(RateReviewDialog rateReviewDialog);

    void Inject(org.nearbyshops.enduserappnew.Lists.CartItemList.Adapter adapter);

    void Inject(CartItemListFragment cartItemListFragment);

    void Inject(CartsListFragment cartsListFragment);

    void Inject(PlaceOrderActivity placeOrderActivity);

    void Inject(FragmentShopsList fragmentShopsList);

    void Inject(EditAddressFragment editAddressFragment);

    void Inject(DeliveryAddressActivity deliveryAddressActivity);

    void Inject(RateReviewDialogMarket rateReviewDialogMarket);

    void Inject(MarketDetailFragment marketDetailFragment);

    void Inject(AdapterMarkets adapterMarkets);

    void Inject(ViewHolderMarketSmall viewHolderMarketSmall);

    void Inject(ViewHolderMarket viewHolderMarket);

    void Inject(ViewModelMarkets viewModelMarkets);

    void Inject(MarketsFragment marketsFragment);

    void Inject(SubmitURLDialog submitURLDialog);

    void Inject(MarketsFragmentNew marketsFragmentNew);

    void Inject(FragmentChangePassword fragmentChangePassword);

    void Inject(FragmentChangeEmail fragmentChangeEmail);

    void Inject(FragmentVerifyEmail fragmentVerifyEmail);

    void Inject(FragmentChangePhone fragmentChangePhone);

    void Inject(FragmentVerifyPhone fragmentVerifyPhone);

    void Inject(FragmentEditProfile fragmentEditProfile);

    void Inject(ProfileFragment profileFragment);

    void Inject(ShopReviews shopReviews);

    void Inject(ShopReviewAdapter shopReviewAdapter);

    void Inject(ShopReviewStats shopReviewStats);

    void Inject(FragmentEmailOrPhone fragmentEmailOrPhone);

    void Inject(FragmentVerify fragmentVerify);

    void Inject(FragmentEnterPassword fragmentEnterPassword);

    void Inject(FragmentEnterCredentials fragmentEnterCredentials);

    void Inject(FragmentCheckResetCode fragmentCheckResetCode);

    void Inject(FragmentResetPassword fragmentResetPassword);


    void Inject(ItemsByCatFragment itemsByCatFragment);

    void Inject(ItemsInShopByCatFragment itemsInShopByCatFragment);


    void Inject(ViewHolderShopItem viewHolderShopItem);

    void Inject(ViewHolderShopItemBackup viewHolderShopItemBackup);

    void Inject(AdapterBackup adapterBackup);

    void Inject(ViewHolderShopItemSeller viewHolderShopItemSeller);

    void Inject(org.nearbyshops.enduserappnew.aSellerModule.ItemsInShopByCatSeller.Adapter adapter);

    void Inject(ItemsInShopFragment itemsInShopFragment);

    void Inject(ItemsDatabaseFragment itemsDatabaseFragment);

    void Inject(ItemsInShopByCatSellerFragment itemsInShopByCatSellerFragment);

    void Inject(OrdersInventoryFragment ordersInventoryFragment);

    void Inject(UsersListFragment usersListFragment);

    void Inject(FilterDeliveryFragment filterDeliveryFragment);

    void Inject(FragmentShopItem fragmentShopItem);

    void Inject(ShopAdminHomeFragment shopAdminHomeFragment);

    void Inject(EditShopFragment editShopFragment);

    void Inject(EditShopImageFragment editShopImageFragment);

    void Inject(TransactionFragment transactionFragment);

    void Inject(EditItemImageFragment editItemImageFragment);

    void Inject(EditItemFragmentNew editItemFragmentNew);

    void Inject(FragmentAddCredit fragmentAddCredit);

    void Inject(AdminDashboardFragment adminDashboardFragment);

    void Inject(EditConfigurationFragment editConfigurationFragment);

    void Inject(EditShopForAdminFragment editShopForAdminFragment);

    void Inject(ItemsDatabaseForAdminFragment itemsDatabaseForAdminFragment);

    void Inject(EditItemCategoryFragment editItemCategoryFragment);

    void Inject(Adapter adapter);

    void Inject(ItemCategoriesParent itemCategoriesParent);


    void Inject(FragmentShopList fragmentShopList);

    void Inject(EditStaffPermissionsFragment editStaffPermissionsFragment);

    void Inject(EditShopStaffPermissionsFragment editShopStaffPermissionsFragment);

    void Inject(AddUserToShopStaffDialog addUserToShopStaffDialog);

    void Inject(DeliveryInventoryFragment deliveryInventoryFragment);

    void Inject(BackupViewHolderMarket backupViewHolderMarket);

    void Inject(BackupViewHolderMarketSmall backupViewHolderMarketSmall);

    void Inject(ViewModelShop viewModelShop);

    void Inject(OrdersListPagingFragment ordersListPagingFragment);

    void Inject(OrdersDataSource ordersDataSource);

    void Inject(ViewHolderShopItemButton viewHolderShopItemButton);

    void Inject(ViewModelUser viewModelUser);

    void Inject(org.nearbyshops.enduserappnew.Lists.CartItemList.Backups.AdapterBackup adapterBackup);

    void Inject(ViewHolderCartItem viewHolderCartItem);

    void Inject(HomeBackup homeBackup);

    void Inject(BackupViewHolderMarketSmall1Mar20 backupViewHolderMarketSmall1Mar20);

    void Inject(LoginGlobalUsingOTPFragment loginGlobalUsingOTPFragment);

    void Inject(AddUserToStaffDialog addUserToStaffDialog);

    void Inject(StaffDashboardFragment staffDashboardFragment);

    void Inject(PlacePickerWithMapFragment placePickerWithMapFragment);

    void Inject(LoginLocalUsingOTPFragmentNew loginLocalUsingOTPFragmentNew);


    void Inject(SDSAdminDashboardFragment SDSAdminDashboardFragment);

    void Inject(MarketsDataSource marketsDataSource);

    void Inject(MarketsListFragment marketsListFragment);
}
