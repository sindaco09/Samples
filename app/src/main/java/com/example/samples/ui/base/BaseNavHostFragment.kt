package com.example.samples.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import com.example.samples.R

private val defaultNavOptions = navOptions {
    anim {
        enter = R.anim.slide_in_from_right
        exit = R.anim.slide_out_to_left
        popEnter = R.anim.slide_in_from_left
        popExit = R.anim.slide_out_to_right
    }
}

private val emptyNavOptions = navOptions {}

class BaseNavHostFragment: NavHostFragment() {

    override fun onCreateNavController(navController: NavController) {
        super.onCreateNavController(navController)
        navController
                .navigatorProvider
                .addNavigator(BaseFragmentNavigator(requireContext(), childFragmentManager, id))
    }
}

@Navigator.Name("fragment")
class BaseFragmentNavigator(
        context: Context,
        manager: FragmentManager,
        containerId: Int
): FragmentNavigator(context, manager, containerId) {

    override fun navigate(
            destination: Destination,
            args: Bundle?,
            navOptions: NavOptions?,
            navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        // this will try to fill in empty animations with defaults when no shared element transitions are set
        // https://developer.android.com/guide/navigation/navigation-animate-transitions#shared-element
        val shouldUseTransitionsInstead = navigatorExtras != null
        val navOptions = if (shouldUseTransitionsInstead) navOptions
        else navOptions.fillEmptyAnimationsWithDefaults()
        return super.navigate(destination, args, navOptions, navigatorExtras)
    }

    private fun NavOptions?.fillEmptyAnimationsWithDefaults(): NavOptions =
            this?.copyNavOptionsWithDefaultAnimations() ?: defaultNavOptions

    private fun NavOptions.copyNavOptionsWithDefaultAnimations(): NavOptions =
            let { originalNavOptions ->
                navOptions {
                    launchSingleTop = originalNavOptions.shouldLaunchSingleTop()
                    popUpTo(originalNavOptions.popUpToId) {
                        inclusive = originalNavOptions.isPopUpToInclusive()
                    }
                    anim {
                        enter =
                                if (originalNavOptions.enterAnim == emptyNavOptions.enterAnim) defaultNavOptions.enterAnim
                                else originalNavOptions.enterAnim
                        exit =
                                if (originalNavOptions.exitAnim == emptyNavOptions.exitAnim) defaultNavOptions.exitAnim
                                else originalNavOptions.exitAnim
                        popEnter =
                                if (originalNavOptions.popEnterAnim == emptyNavOptions.popEnterAnim) defaultNavOptions.popEnterAnim
                                else originalNavOptions.popEnterAnim
                        popExit =
                                if (originalNavOptions.popExitAnim == emptyNavOptions.popExitAnim) defaultNavOptions.popExitAnim
                                else originalNavOptions.popExitAnim
                    }
                }
            }
}