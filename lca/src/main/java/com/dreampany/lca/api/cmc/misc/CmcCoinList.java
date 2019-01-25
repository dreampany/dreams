package com.dreampany.lca.api.cmc.misc;

import androidx.annotation.NonNull;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Predicate;
import com.dreampany.lca.api.cmc.model.CmcCoin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Hawladar Roman on 2/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class CmcCoinList implements List<CmcCoin> {

    private final List<CmcCoin> coins;

    public CmcCoinList(Collection<CmcCoin> coins) {
        if (coins == null) {
            throw new NullPointerException("coins argument may not be null.");
        }
        this.coins = Collections.unmodifiableList(new ArrayList<>(coins));
    }

    private Optional<CmcCoin> find(Predicate<CmcCoin> predicate) {
        return Stream.of(coins).filter(predicate).findFirst();
    }

    public Optional<CmcCoin> getById(long id) {
        return find(coin -> coin.getId() == id);
    }

    public Optional<CmcCoin> getByName(String name) {
        return find(coin -> coin.getName().equalsIgnoreCase(name));
    }

    public Optional<CmcCoin> getBySymbol(String symbol) {
        return find(coin -> coin.getSymbol().equalsIgnoreCase(symbol));
    }

    public Optional<CmcCoin> getByWebsiteSlug(String websiteSlug) {
        return find(coin -> coin.getSlug().equalsIgnoreCase(websiteSlug));
    }

    @Override
    public int size() {
        return coins.size();
    }

    @Override
    public boolean isEmpty() {
        return coins.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return coins.contains(o);
    }

    @NonNull
    @Override
    public Iterator<CmcCoin> iterator() {
        return coins.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return coins.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return coins.toArray(ts);
    }

    @Override
    public boolean add(CmcCoin CMCCoin) {
        return coins.add(CMCCoin);
    }

    @Override
    public boolean remove(Object o) {
        return coins.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return coins.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends CmcCoin> collection) {
        return coins.addAll(collection);
    }

    @Override
    public boolean addAll(int i, @NonNull Collection<? extends CmcCoin> collection) {
        return coins.addAll(i, collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return coins.removeAll(collection);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return coins.retainAll(collection);
    }

    @Override
    public void clear() {
        coins.clear();
    }

    @Override
    public CmcCoin get(int i) {
        return coins.get(i);
    }

    @Override
    public CmcCoin set(int i, CmcCoin CMCCoin) {
        return coins.set(i, CMCCoin);
    }

    @Override
    public void add(int i, CmcCoin CMCCoin) {
        coins.add(i, CMCCoin);
    }

    @Override
    public CmcCoin remove(int i) {
        return coins.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return coins.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return coins.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<CmcCoin> listIterator() {
        return coins.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<CmcCoin> listIterator(int i) {
        return coins.listIterator(i);
    }

    @NonNull
    @Override
    public List<CmcCoin> subList(int fromIndex, int toIndex) {
        return coins.subList(fromIndex, toIndex);
    }
}
