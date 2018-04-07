package com.kingja.rxre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Description:TODO
 * Create Time:2018/4/7 21:42
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class RxRe {
    public static RxRe mInstance;
    public Map<Class<?>, ArrayList<Disposable>> mDisposablePool = new HashMap<>();

    public static RxRe getInstance() {
        if (mInstance == null) {
            synchronized (RxRe.class) {
                if (mInstance == null) {
                    mInstance = new RxRe();
                }
            }
        }
        return mInstance;
    }

    public void add(Object tag, Disposable disposable) {
        Class<?> clazz = tag.getClass();
        if (!mDisposablePool.containsKey(clazz)) {
            mDisposablePool.put(clazz, new ArrayList<Disposable>());
        }
        mDisposablePool.get(clazz).add(disposable);
    }

    public void cancle(Object tag) {
        cancle(tag.getClass());
    }

    private void cancle(Class<?> disposableClass) {
        if (mDisposablePool.containsKey(disposableClass)) {
            ArrayList<Disposable> disposables = mDisposablePool.get(disposableClass);
            for (Disposable disposable : disposables) {
                if (!disposable.isDisposed()) {
                    disposable.dispose();
                }
            }
            disposables.clear();
            mDisposablePool.remove(disposableClass);
        }
    }

    public void cancleAll() {
        if (!mDisposablePool.isEmpty()) {
            Set<Class<?>> keySet = mDisposablePool.keySet();
            for (Class<?> clazz : keySet) {
                cancle(clazz);
            }
        }
    }
}
