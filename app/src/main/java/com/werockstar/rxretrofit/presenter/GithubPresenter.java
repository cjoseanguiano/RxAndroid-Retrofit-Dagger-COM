package com.werockstar.rxretrofit.presenter;

import com.werockstar.rxretrofit.api.GithubAPI;
import com.werockstar.rxretrofit.model.GithubCollection;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class GithubPresenter {

    private CompositeSubscription subscription = new CompositeSubscription();
    private GithubAPI api;
    private GithubPresenter.View view;

    public interface View {
        void showGithubInfo(GithubCollection collection);

        void onCompleted();

        void onError(Throwable t);
    }

    public GithubPresenter(GithubPresenter.View view, GithubAPI api) {
        this.view = view;
        this.api = api;
    }

    /*public void getGithubInfo(String username) {
        subscription.add(api.getGithubInfo(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        github -> view.showGithubInfo(github),
                        throwable -> view.onError(throwable),
                        () -> view.onCompleted()
                ));
    }*/

    public void getGithubInfo(String username) {
        subscription.add(api.getGithubInfo(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GithubCollection>() {
                    @Override
                    public void onCompleted() {
                        view.onCompleted();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        view.onError(throwable);
                    }

                    @Override
                    public void onNext(GithubCollection github) {
                        view.showGithubInfo(github);
                    }
                }));
    }

    public void onStop() {
        subscription.clear();
    }

}
