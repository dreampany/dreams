package com.dreampany.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.ActionCodeResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by Hawladar Roman on 5/29/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class RxFirebaseAuth {


    @NonNull
    public static Maybe<AuthResult> signInAnonymously(@NonNull final FirebaseAuth firebaseAuth) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.signInAnonymously()));
    }

    @NonNull
    public static Maybe<AuthResult> signInWithEmailAndPassword(@NonNull final FirebaseAuth firebaseAuth,
                                                               @NonNull final String email,
                                                               @NonNull final String password) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.signInWithEmailAndPassword(email, password)));
    }

    @NonNull
    public static Maybe<AuthResult> signInWithCredential(@NonNull final FirebaseAuth firebaseAuth,
                                                         @NonNull final AuthCredential credential) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.signInWithCredential(credential)));
    }

    @NonNull
    public static Maybe<AuthResult> signInWithCustomToken(@NonNull final FirebaseAuth firebaseAuth,
                                                          @NonNull final String token) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.signInWithCustomToken(token)));
    }

    @NonNull
    public static Maybe<AuthResult> createUserWithEmailAndPassword(@NonNull final FirebaseAuth firebaseAuth,
                                                                   @NonNull final String email,
                                                                   @NonNull final String password) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.createUserWithEmailAndPassword(email, password)));
    }

    @NonNull
    public static Maybe<SignInMethodQueryResult> fetchSignInMethodsForEmail(@NonNull final FirebaseAuth firebaseAuth,
                                                                            @NonNull final String email) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.fetchSignInMethodsForEmail(email)));
    }

    @NonNull
    public static Completable sendPasswordResetEmail(@NonNull final FirebaseAuth firebaseAuth,
                                                     @NonNull final String email) {
        return Completable.create(emitter -> RxCompletableHandler.assignOnTask(emitter, firebaseAuth.sendPasswordResetEmail(email)));
    }

    @NonNull
    public static Completable updateCurrentUser(@NonNull final FirebaseAuth firebaseAuth,
                                                @NonNull final FirebaseUser newUser) {
        return Completable.create(emitter -> RxCompletableHandler.assignOnTask(emitter, firebaseAuth.updateCurrentUser(newUser)));
    }

    @NonNull
    public static Observable<FirebaseAuth> observeAuthState(@NonNull final FirebaseAuth firebaseAuth) {
        return Observable.create(emitter -> {
            FirebaseAuth.AuthStateListener authStateListener = emitter::onNext;
            firebaseAuth.addAuthStateListener(authStateListener);
            emitter.setCancellable(() -> firebaseAuth.removeAuthStateListener(authStateListener));
        });
    }

    @NonNull
    public static Maybe<ActionCodeResult> checkActionCode(@NonNull final FirebaseAuth firebaseAuth,
                                                          @NonNull final String code) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.checkActionCode(code)));
    }

    @NonNull
    public static Completable confirmPasswordReset(@NonNull final FirebaseAuth firebaseAuth,
                                                   @NonNull final String code,
                                                   @NonNull final String newPassword) {
        return Completable.create(emitter -> RxCompletableHandler.assignOnTask(emitter, firebaseAuth.confirmPasswordReset(code, newPassword)));
    }

    @NonNull
    public static Completable applyActionCode(@NonNull final FirebaseAuth firebaseAuth,
                                              @NonNull final String code) {
        return Completable.create(emitter -> RxCompletableHandler.assignOnTask(emitter, firebaseAuth.applyActionCode(code)));
    }

    @NonNull
    public static Maybe<String> verifyPasswordResetCode(@NonNull final FirebaseAuth firebaseAuth,
                                                        @NonNull final String code) {
        return Maybe.create(emitter -> RxHandler.assignOnTask(emitter, firebaseAuth.verifyPasswordResetCode(code)));
    }

    @NonNull
    public static Completable addIdTokenListener(@NonNull final FirebaseAuth firebaseAuth,
                                                 @NonNull final FirebaseAuth.IdTokenListener idTokenListener) {
        return Completable.create(emitter -> {
            firebaseAuth.addIdTokenListener(idTokenListener);
            emitter.onComplete();
        });
    }

    @NonNull
    public static Completable removeIdTokenListener(@NonNull final FirebaseAuth firebaseAuth,
                                                    @NonNull final FirebaseAuth.IdTokenListener idTokenListener) {
        return Completable.create(emitter -> {
            firebaseAuth.removeIdTokenListener(idTokenListener);
            emitter.onComplete();
        });
    }
}
