package org.nearbyshops.whitelabelapp.DI.DaggerComponents;


import dagger.Component;

import org.jetbrains.annotations.NotNull;
import org.nearbyshops.whitelabelapp.Admin.ShopsListForAdmin.Fragment.FragmentShopList;
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelDeliveryGuy;
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelMarketsForAdmin;
import org.nearbyshops.whitelabelapp.Admin.ViewModel.ViewModelShopForAdmin;
import org.nearbyshops.whitelabelapp.AdminCommon.BillGenerator;
import org.nearbyshops.whitelabelapp.AdminShop.zBottomDashboard.Deprecated.ShopDashboardFragment;
import org.nearbyshops.whitelabelapp.AdminShop.ShopDashboardBottom;
import org.nearbyshops.whitelabelapp.AdminShop.zBottomDashboard.Deprecated.FragmentItemsInShop;
import org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress.DeliveryAddressSelectionFragment;
import org.nearbyshops.whitelabelapp.CartAndOrder.Checkout.FragmentPlaceOrder;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditMarket.EditMarketFragment;
import org.nearbyshops.whitelabelapp.Home;
import org.nearbyshops.whitelabelapp.Lists.MarketsList.ListUIFragment;
import org.nearbyshops.whitelabelapp.Lists.MarketsList.ShopHomeTypeList;
import org.nearbyshops.whitelabelapp.aaListUI.ViewModels.ViewModelQuickStockEditor;
import org.nearbyshops.whitelabelapp.aaMultimarketFiles.EditDataScreens.EditMarketSettingsMM.EditMarketSettingsKotlin;
import org.nearbyshops.whitelabelapp.aaListUI.ViewModels.ViewModelDelegator;
import org.nearbyshops.whitelabelapp.aaListUI.ViewModels.ViewModelOrderDetail;
import org.nearbyshops.whitelabelapp.CartAndOrder.Checkout.SelectPaymentFragment;
import org.nearbyshops.whitelabelapp.DI.DaggerModules.AppModule;
import org.nearbyshops.whitelabelapp.DI.DaggerModules.NetModule;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailItemNew.ItemDetailFragmentNew;
import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.CallSupportDialog;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopItem.ShopItemDetailFragment;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShopNew.ShopDetailFragmentNew;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditBannerImage.EditBannerImageFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditAddressWithMapFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.AddShopFragment;
import org.nearbyshops.whitelabelapp.AdminDelivery.ButtonDashboard.FragmentDeprecated.DeliveryInventoryFragment;
import org.nearbyshops.whitelabelapp.AdminDelivery.InventoryDeliveryPerson.Fragment.DeliveryFragmentNew;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.ViewHolderDeliverySlot;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.ViewModelDeliverySlot;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditDeliveryAddress.EditAddressFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemCategory.EditItemCategoryFragment;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.FragmentDeprecated.InventoryHDFragment;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryHomeDelivery.Fragment.InventoryHDFragmentNew;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryPickFromShop.FragmentDeprecated.InventoryPFSFragment;
import org.nearbyshops.whitelabelapp.InventoryOrders.InventoryPickFromShop.Fragment.InventoryPFSFragmentNew;
import org.nearbyshops.whitelabelapp.InventoryOrders.ViewModelOrders;
import org.nearbyshops.whitelabelapp.Lists.ShopsAvailableNew.ShopsAvailableFragment;
import org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.AddUserToStaffDialogNew;
import org.nearbyshops.whitelabelapp.Services.GetAppSettings;
import org.nearbyshops.whitelabelapp.Services.NonStopServices.PersistentLocationService;
import org.nearbyshops.whitelabelapp.UtilityScreens.BannerSlider.ViewHolderBannerListItem;
import org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps.AddressPickerFragment;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItemInstacart;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelItemDetail;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShopDetail;
import org.nearbyshops.whitelabelapp.InventoryOrders.Deprecated.InventoryDeliveryByVendor.Fragment.DeliveryByVendorFragment;
import org.nearbyshops.whitelabelapp.AdminCommon.PushNotificationComposer;
import org.nearbyshops.whitelabelapp.AdminCommon.SalesReport.SalesReportFragment;
import org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.Deprecated.AddUserToStaffDialog;
import org.nearbyshops.whitelabelapp.Login.LoginUsingOTPFragment;
import org.nearbyshops.whitelabelapp.PlacePickers.PlacePickerGoogleMaps.Deprecated.PlacePickerWithMapFragment;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderCartItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderCartItemNew;
import org.nearbyshops.whitelabelapp.zSampleCode.OrderHistoryPaging.ViewModel.OrdersDataSource;
import org.nearbyshops.whitelabelapp.zSampleCode.OrderHistoryPaging.OrdersListPagingFragment;
import org.nearbyshops.whitelabelapp.Lists.UsersList.Dialogs.Deprecated.AddUserToShopStaffDialog;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShopStaffPermissions.EditShopStaffPermissionsFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditStaffPermissions.EditStaffPermissionsFragment;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItem;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItemBackup;
import org.nearbyshops.whitelabelapp.ViewHolders.ViewHolderShopItem.ViewHolderShopItemButton;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelShop;
import org.nearbyshops.whitelabelapp.InventoryOrders.FilterDeliveryGuy.FilterDeliveryFragment;
import org.nearbyshops.whitelabelapp.ViewModels.ViewModelUser;
import org.nearbyshops.whitelabelapp.AdminCommon.AddCredit.FragmentAddCredit;
import org.nearbyshops.whitelabelapp.CartAndOrder.CartItemList.CartItemListFragment;
import org.nearbyshops.whitelabelapp.CartAndOrder.CartsList.CartsListFragment;
import org.nearbyshops.whitelabelapp.CartAndOrder.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.whitelabelapp.InventoryOrders.DeliverySlot.EditDeliverySlot.EditDeliverySlotFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItem.EditItemFragmentNew;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditItemImage.EditItemImageFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangeEmail.FragmentChangeEmail;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangeEmail.FragmentVerifyEmail;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangePassword.FragmentChangePassword;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangePhone.FragmentChangePhone;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.ChangePhone.FragmentVerifyPhone;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditProfile.FragmentEditProfile;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShop.EditShopFragment;
import org.nearbyshops.whitelabelapp.EditDataScreens.EditShopImage.EditShopImageFragment;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailItem.ItemDetailFragment;
import org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForItem.ItemImageListFragment;
import org.nearbyshops.whitelabelapp.Lists.ItemsByCategory.ItemsByCatFragment;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForShop.ItemsDatabaseFragment;
import org.nearbyshops.whitelabelapp.AdminShop.ItemsInShopByCatSeller.ItemsInShopByCatSellerFragment;
import org.nearbyshops.whitelabelapp.AdminShop.ItemsInShopSeller.ItemsInShopFragment;
import org.nearbyshops.whitelabelapp.AdminShop.QuickStockEditor.FragmentShopItem;
import org.nearbyshops.whitelabelapp.AdminShop.ButtonDashboard.DashboardShopAdmin.ShopAdminHomeFragment;
import org.nearbyshops.whitelabelapp.Lists.UsersList.UsersListFragment;
import org.nearbyshops.whitelabelapp.Lists.TransactionHistory.TransactionFragment;
import org.nearbyshops.whitelabelapp.AdminShop.ViewHolders.ViewHolderShopItemSeller;
import org.nearbyshops.whitelabelapp.Lists.ItemsInShopByCategory.ItemsInShopByCatFragment;
import org.nearbyshops.whitelabelapp.Login.Deprecated.LoginUsingPasswordFragment;
import org.nearbyshops.whitelabelapp.PushOneSignal.UpdateOneSignalID;
import org.nearbyshops.whitelabelapp.CartAndOrder.DetailOrder.FragmentOrderDetail;
import org.nearbyshops.whitelabelapp.CartAndOrder.OrderHistory.OrdersHistoryFragment;
import org.nearbyshops.whitelabelapp.zDeprecatedScreens.ProfileFragment;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShop.RateReviewDialog;
import org.nearbyshops.whitelabelapp.DetailScreens.DetailShop.ShopDetailFragment;
import org.nearbyshops.whitelabelapp.ImageScreens.ImageList.ImageListForShop.ShopImageListFragment;
import org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem.Backup.AdapterBackup;
import org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem.ShopItemFragment;
import org.nearbyshops.whitelabelapp.ShopReview.ShopReviewAdapter;
import org.nearbyshops.whitelabelapp.ShopReview.ShopReviewStats;
import org.nearbyshops.whitelabelapp.ShopReview.ShopReviews;
import org.nearbyshops.whitelabelapp.Lists.ShopsList.FragmentShopsList;
import org.nearbyshops.whitelabelapp.Login.SignUp.ForgotPassword.FragmentCheckResetCode;
import org.nearbyshops.whitelabelapp.Login.SignUp.ForgotPassword.FragmentEnterCredentials;
import org.nearbyshops.whitelabelapp.Login.SignUp.ForgotPassword.FragmentResetPassword;
import org.nearbyshops.whitelabelapp.Login.SignUp.FragmentEmailOrPhone;
import org.nearbyshops.whitelabelapp.Login.SignUp.FragmentEnterPassword;
import org.nearbyshops.whitelabelapp.Login.SignUp.FragmentVerify;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ItemsDatabaseForAdmin.ItemsDatabaseForAdminFragment;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ChangeParent.Adapter;
import org.nearbyshops.whitelabelapp.AdminCommon.ItemsDatabase.ChangeParent.ItemCategoriesParent;


import javax.inject.Singleton;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {


    void Inject(UpdateOneSignalID updateOneSignalID);

    void Inject(org.nearbyshops.whitelabelapp.Lists.ShopsAvailableForItem.Adapter adapter);

    void Inject(ShopItemFragment shopItemFragment);

    void Inject(GetAppSettings getAppSettings);

    void Inject(ItemDetailFragment itemDetailFragment);

    void Inject(ItemImageListFragment itemImageListFragment);

    void Inject(LoginUsingPasswordFragment loginUsingPasswordFragment);

    void Inject(OrdersHistoryFragment ordersHistoryFragment);

    void Inject(FragmentOrderDetail fragmentOrderDetail);

    void Inject(ShopDetailFragment shopDetailFragment);

    void Inject(ShopImageListFragment shopImageListFragment);

    void Inject(RateReviewDialog rateReviewDialog);

    void Inject(org.nearbyshops.whitelabelapp.CartAndOrder.CartItemList.Adapter adapter);

    void Inject(CartItemListFragment cartItemListFragment);

    void Inject(CartsListFragment cartsListFragment);

    void Inject(FragmentShopsList fragmentShopsList);

    void Inject(EditDeliverySlotFragment editDeliverySlotFragment);

    void Inject(DeliveryAddressActivity deliveryAddressActivity);

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

    void Inject(org.nearbyshops.whitelabelapp.AdminShop.ItemsInShopByCatSeller.Adapter adapter);

    void Inject(ItemsInShopFragment itemsInShopFragment);

    void Inject(ItemsDatabaseFragment itemsDatabaseFragment);

    void Inject(ItemsInShopByCatSellerFragment itemsInShopByCatSellerFragment);

    void Inject(InventoryHDFragment inventoryHDFragment);

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

    void Inject(ItemsDatabaseForAdminFragment itemsDatabaseForAdminFragment);

    void Inject(EditItemCategoryFragment editItemCategoryFragment);

    void Inject(Adapter adapter);

    void Inject(ItemCategoriesParent itemCategoriesParent);

    void Inject(EditStaffPermissionsFragment editStaffPermissionsFragment);

    void Inject(EditShopStaffPermissionsFragment editShopStaffPermissionsFragment);

    void Inject(AddUserToShopStaffDialog addUserToShopStaffDialog);

    void Inject(DeliveryByVendorFragment deliveryByVendorFragment);

    void Inject(ViewModelShop viewModelShop);

    void Inject(OrdersListPagingFragment ordersListPagingFragment);

    void Inject(OrdersDataSource ordersDataSource);

    void Inject(ViewHolderShopItemButton viewHolderShopItemButton);

    void Inject(ViewModelUser viewModelUser);

    void Inject(ViewHolderCartItem viewHolderCartItem);

    void Inject(PlacePickerWithMapFragment placePickerWithMapFragment);

    void Inject(LoginUsingOTPFragment loginUsingOTPFragment);

    void Inject(ViewHolderCartItemNew viewHolderCartItemNew);

    void Inject(ViewModelDeliverySlot viewModelDeliverySlot);

    void Inject(EditAddressFragment editAddressFragment);

    void Inject(DeliveryInventoryFragment deliveryInventoryFragment);

    void Inject(InventoryPFSFragment inventoryPFSFragment);

    void Inject(ViewHolderDeliverySlot viewHolderDeliverySlot);

    void Inject(InventoryHDFragmentNew inventoryHDFragmentNew);

    void Inject(ViewModelOrders viewModelOrders);

    void Inject(DeliveryFragmentNew deliveryFragmentNew);

    void Inject(InventoryPFSFragmentNew inventoryPFSFragmentNew);

    void Inject(ItemDetailFragmentNew itemDetailFragmentNew);

    void Inject(ViewHolderShopItemInstacart viewHolderShopItemInstacart);

    void Inject(ViewModelItemDetail viewModelItemDetail);

    void Inject(SelectPaymentFragment selectPaymentFragment);

    void Inject(ViewModelShopDetail viewModelShopDetail);

    void Inject(ShopDetailFragmentNew shopDetailFragmentNew);

    void Inject(ShopItemDetailFragment shopItemDetailFragment);

    void Inject(ShopsAvailableFragment shopsAvailableFragment);

    void Inject(AddressPickerFragment addressPickerFragment);

    void Inject(EditAddressWithMapFragment editAddressWithMapFragment);

    void Inject(PushNotificationComposer pushNotificationComposer);

    void Inject(EditBannerImageFragment editBannerImageFragment);

    void Inject(ViewHolderBannerListItem viewHolderBannerListItem);

    void Inject(AddShopFragment addShopFragment);

    void Inject(SalesReportFragment salesReportFragment);

    void Inject(PersistentLocationService persistentLocationService);

    void Inject(AddUserToStaffDialog addUserToStaffDialog);

    void Inject(CallSupportDialog callSupportDialog);

    void Inject(AddUserToStaffDialogNew addUserToStaffDialogNew);


    void Inject(Home home);


    void Inject(@NotNull DeliveryAddressSelectionFragment deliveryAddressSelectionFragment);

    void Inject(@NotNull FragmentPlaceOrder fragmentPlaceOrder);

    void Inject(@NotNull ListUIFragment listUIFragment);

    void Inject(@NotNull ViewModelDelegator viewModelDelegator);

    void Inject(@NotNull ViewModelOrderDetail viewModelOrderDetail);

    void Inject(ShopDashboardBottom shopDashboardBottom);

    void Inject(ShopDashboardFragment shopDashboardFragment);

    void Inject(FragmentItemsInShop fragmentItemsInShop);

    void Inject(@NotNull ViewModelQuickStockEditor viewModelQuickStockEditor);

    void Inject(@NotNull ShopHomeTypeList shopHomeTypeList);

    void Inject(@NotNull org.nearbyshops.whitelabelapp.Admin.MarketHome marketHome);

    void Inject(@NotNull EditMarketSettingsKotlin editMarketSettingsKotlin);

    void Inject(ViewModelShopForAdmin viewModelShopForAdmin);

    void Inject(FragmentShopList fragmentShopList);

    void Inject(EditMarketFragment editMarketFragment);

    void Inject(ViewModelMarketsForAdmin viewModelMarketsForAdmin);

    void Inject(ViewModelDeliveryGuy viewModelDeliveryGuy);

    void Inject(BillGenerator billGenerator);
}
