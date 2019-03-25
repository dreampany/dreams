package com.dreampany.lca.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreampany.frame.misc.ActivityScope;
import com.dreampany.frame.ui.fragment.BaseFragment;
import com.dreampany.frame.util.AndroidUtil;
import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;

import com.dreampany.frame.misc.Constants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by Hawladar Roman on 6/18/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@ActivityScope
public class AboutFragment extends BaseFragment {

    @Inject
    public AboutFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setTitle(R.string.title_about);
        Context context = inflater.getContext();
        AboutPage page = new AboutPage(context)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(TextUtil.getString(context, R.string.app_name))
                .addItem(getVersion(context))
                .addItem(getPrivacyPolicy(context))
                .addGroup("Connect with us")
                .addEmail(TextUtil.getString(context, R.string.email))
                .addWebsite(TextUtil.getString(context, R.string.website))
                .addPlayStore(AndroidUtil.getPackageName(context))
                .addGitHub(TextUtil.getString(context, R.string.id_github))
                /*.addItem(getYandexTranslation(context))*/;

        return page.create();
    }

    @NotNull
    @Override
    public String getScreen() {
        return Constants.Screen.about(getAppContext());
    }

    @Override
    protected void onStartUi(@Nullable Bundle state) {

    }

    @Override
    protected void onStopUi() {

    }

    private Element getVersion(Context context) {
        String version = AndroidUtil.getVersionName(context);
        Element element = new Element()
                .setTitle(TextUtil.getString(context, R.string.summary_app_version, version));
        return element;
    }

    private Element getPrivacyPolicy(Context context) {
        Element element = new Element()
                .setTitle(TextUtil.getString(context, R.string.title_privacy_policy))
                .setOnClickListener(view -> {
                    String url = TextUtil.getString(context, R.string.url_privacy);
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                    urlIntent.setData(Uri.parse(url));
                    startActivity(urlIntent);
                });
        return element;
    }
}
