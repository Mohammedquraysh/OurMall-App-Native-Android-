package eu.ourmall.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import eu.ourmall.app.presentation.screen.cart.CartScreen
import eu.ourmall.app.presentation.screen.checkout.CheckoutScreen
import eu.ourmall.app.presentation.screen.order.OrderDetailScreen
import eu.ourmall.app.presentation.screen.order.OrderListScreen
import eu.ourmall.app.presentation.screen.products.ProductDetailScreen
import eu.ourmall.app.presentation.screen.products.ProductListScreen
import eu.ourmall.app.presentation.theme.scaleInFade
import eu.ourmall.app.presentation.theme.scaleOutFade
import eu.ourmall.app.presentation.theme.slideInFromLeft
import eu.ourmall.app.presentation.theme.slideInFromRight
import eu.ourmall.app.presentation.theme.slideOutToLeft
import eu.ourmall.app.presentation.theme.slideOutToRight

sealed class Screen(val route: String) {
    object ProductList   : Screen("products")
    object ProductDetail : Screen("products/{productId}") {
        fun withId(id: String) = "products/$id"
    }
    object Cart          : Screen("cart")
    object Checkout      : Screen("checkout")
    object OrderList     : Screen("orders")
    object OrderDetail   : Screen("orders/{orderId}") {
        fun withId(id: String) = "orders/$id"
    }
}

@Composable
fun OurMallNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route,
    ) {
        composable(
            route = Screen.ProductList.route,
            enterTransition  = { slideInFromRight() },
            exitTransition   = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition  = { slideOutToRight() },
        ) {
            ProductListScreen(
                onProductClick = { navController.navigate(Screen.ProductDetail.withId(it)) },
                onCartClick    = { navController.navigate(Screen.Cart.route) },
                onOrdersClick  = { navController.navigate(Screen.OrderList.route) },
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType }),
            enterTransition  = { scaleInFade() },
            exitTransition   = { scaleOutFade() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition  = { slideOutToRight() },
        ) { back ->
            ProductDetailScreen(
                productId = back.arguments!!.getString("productId")!!,
                onBack    = { navController.popBackStack() },
                onCartClick = { navController.navigate(Screen.Cart.route) },
            )
        }

        composable(
            route = Screen.Cart.route,
            enterTransition  = { slideInFromRight() },
            exitTransition   = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition  = { slideOutToRight() },
        ) {
            CartScreen(
                onBack     = { navController.popBackStack() },
                onCheckout = { navController.navigate(Screen.Checkout.route) },
            )
        }

        composable(
            route = Screen.Checkout.route,
            enterTransition  = { slideInFromRight() },
            exitTransition   = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition  = { slideOutToRight() },
        ) {
            CheckoutScreen(
                onBack       = { navController.popBackStack() },
                onOrderPlaced = { orderId ->
                    navController.navigate(Screen.OrderDetail.withId(orderId)) {
                        popUpTo(Screen.ProductList.route)
                    }
                },
            )
        }

        composable(
            route = Screen.OrderList.route,
            enterTransition  = { slideInFromRight() },
            exitTransition   = { slideOutToLeft() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition  = { slideOutToRight() },
        ) {
            OrderListScreen(
                onBack        = { navController.popBackStack() },
                onOrderClick  = { navController.navigate(Screen.OrderDetail.withId(it)) },
            )
        }

        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType }),
            enterTransition  = { scaleInFade() },
            exitTransition   = { scaleOutFade() },
            popEnterTransition = { slideInFromLeft() },
            popExitTransition  = { slideOutToRight() },
        ) { back ->
            OrderDetailScreen(
                orderId = back.arguments!!.getString("orderId")!!,
                onBack  = { navController.popBackStack() },
            )
        }
    }
}
