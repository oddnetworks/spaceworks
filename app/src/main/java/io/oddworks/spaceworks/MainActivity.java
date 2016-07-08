package io.oddworks.spaceworks;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.oddworks.device.model.Config;
import io.oddworks.device.model.Media;
import io.oddworks.device.model.OddCollection;
import io.oddworks.device.model.OddView;
import io.oddworks.device.model.Promotion;
import io.oddworks.device.request.CachingApiCaller;
import io.oddworks.device.request.OddCallback;
import io.oddworks.device.request.RestServiceProvider;

public class MainActivity extends AppCompatActivity {

    private static final String HOME_VIEW = "homepage";
    private static final String FEATURED_MEDIA = "featuredMedia";
    private static final String FEATURED_COLLECTIONS = "featuredCollections";
    private static final String PROMOTION = "promotion";
    private static final String ENTITIES = "entities";

    @BindView(R.id.loadingLayout) View loadingLayout;
    @BindView(R.id.dataLayout) View dataLayout;
    @BindView(R.id.promotionTitle) TextView promotionTitle;
    @BindView(R.id.featuredMediaTitle) TextView featuredMediaTitle;
    @BindView(R.id.featuredCollectionsSize) TextView featuredCollectionsSize;
    @BindView(R.id.promotionImage) ImageView promotionImage;
    @BindView(R.id.featuredMediaImage) ImageView featuredMediaImage;
    @BindView(R.id.featuredCollectionsImage) ImageView featuredCollectionsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initActivity();
    }

    private void initActivity() {
        hideData();
        fetchData();
    }

    private void fetchData() {
        CachingApiCaller apiCaller = RestServiceProvider.getInstance().getCachingApiCaller();

        apiCaller.getConfig(new OddCallback<Config>() {
            @Override
            public void onSuccess(Config config) {
                handleConfig(config);
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("MainActivity", exception.toString());
                displayError();
            }
        }, false);
    }

    private void handleConfig(Config config) {
        LinkedHashMap<String, String> views = config.getViews();
        String homeView = views.get(HOME_VIEW);

        fetchHomeView(homeView);
    }

    private void fetchHomeView(final String homeView) {
        CachingApiCaller apiCaller = RestServiceProvider.getInstance().getCachingApiCaller();

        ArrayList<String> included = new ArrayList<>(Arrays.asList(PROMOTION, FEATURED_MEDIA, FEATURED_COLLECTIONS));
        apiCaller.getView(homeView, included, new OddCallback<OddView>() {
            @Override
            public void onSuccess(final OddView view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handleHomeView(view);
                    }
                });
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("MainActivity", exception.toString());
                displayError();
            }
        }, false);
    }

    private void handleHomeView(OddView homeView) {
        Promotion promotion = (Promotion) homeView.getIncludedByRelationship(PROMOTION).get(0);
        Media featuredMedia = (Media) homeView.getIncludedByRelationship(FEATURED_MEDIA).get(0);
        OddCollection featuredCollections = (OddCollection) homeView.getIncludedByRelationship(FEATURED_COLLECTIONS).get(0);

        promotionTitle.setText(promotion.getTitle());
        Picasso.with(this).load(promotion.getImages().get(0).getUrl()).placeholder(android.R.color.darker_gray).into(promotionImage);
        featuredMediaTitle.setText(featuredMedia.getTitle());
        Picasso.with(this).load(featuredMedia.getImages().get(0).getUrl()).placeholder(android.R.color.darker_gray).into(featuredMediaImage);
        int count = featuredCollections.getIdentifiersByRelationship(ENTITIES).size();
        String size = count + " collections";
        featuredCollectionsSize.setText(size);
        Picasso.with(this).load(featuredCollections.getImages().get(0).getUrl()).placeholder(android.R.color.darker_gray).into(featuredCollectionsImage);

        showData();
    }

    private void displayError() {
        Snackbar.make(promotionTitle, "Error Fetching Data", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivity();
            }
        }).show();
    }

    private void hideData() {
        loadingLayout.setVisibility(View.VISIBLE);
        dataLayout.setVisibility(View.GONE);
    }

    private void showData() {
        loadingLayout.setVisibility(View.GONE);
        dataLayout.setVisibility(View.VISIBLE);
    }
}
