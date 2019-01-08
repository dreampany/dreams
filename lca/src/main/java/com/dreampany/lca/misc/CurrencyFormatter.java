package com.dreampany.lca.misc;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.dreampany.lca.R;
import com.dreampany.lca.api.cmc.enums.Currency;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Hawladar Roman on 6/12/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
@Singleton
public class CurrencyFormatter {

    @NonNull
    private final Context context;
    @NonNull
    private final Map<Currency, String> formats;
    @NonNull
    private Set<Currency> cryptos;
    @NonNull
    private DecimalFormat cryptoFormatter;

    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final String NAMES[] = new String[]{"T", "M", "B", "T"};
    private static final NavigableMap<BigInteger, String> MAP;

    static {
        MAP = new TreeMap<>();
        for (int i = 0; i < NAMES.length; i++) {
            MAP.put(THOUSAND.pow(i + 1), NAMES[i]);
        }
    }

    @Inject
    public CurrencyFormatter(Context context) {
        this.context = context;
        formats = Maps.newHashMap();
        cryptos = Sets.newHashSet(Currency.getCryptoCurrencies());
        cryptoFormatter = new DecimalFormat(context.getString(R.string.crypto_formatter));

        loadFormats();
    }

    private void loadFormats() {
        formats.put(Currency.BTC, context.getString(R.string.btc_format));
        formats.put(Currency.ETH, context.getString(R.string.eth_format));
        formats.put(Currency.LTC, context.getString(R.string.ltc_format));

        formats.put(Currency.AUD, getString(R.string.aud_format));
        formats.put(Currency.BRL, getString(R.string.brl_format));
        formats.put(Currency.CAD, getString(R.string.usd_format));
        formats.put(Currency.CHF, getString(R.string.chf_format));
        formats.put(Currency.CLP, getString(R.string.usd_format));
        formats.put(Currency.CNY, getString(R.string.cny_format));
        formats.put(Currency.CZK, getString(R.string.czk_format));
        formats.put(Currency.DKK, getString(R.string.dkk_format));
        formats.put(Currency.GBP, getString(R.string.gbp_format));
        formats.put(Currency.USD, getString(R.string.usd_format));
    }

    @NonNull
    private String getString(@StringRes int resId) {
        return context.getString(resId);
    }

    public String getCryptoString(double price) {
        return cryptoFormatter.format(price);
    }

    public String format(Currency currency, double price) {
        if (cryptos.contains(currency)) {
            String priceValue = getCryptoString(price);
            return String.format(formats.get(currency), priceValue);
        }
        String format = formats.get(currency);
        if (format != null) {
            return String.format(format, price);
        }
        NumberFormat nf = NumberFormat.getInstance(context.getResources().getConfiguration().locale);
        nf.setMaximumFractionDigits(10);
        return currency.name() + " " + nf.format(price);
    }

    public String format(String currencyValue, double price) {
        NumberFormat nf = NumberFormat.getInstance(context.getResources().getConfiguration().locale);
        nf.setMaximumFractionDigits(10);
        return currencyValue + " " + nf.format(price);
    }

    public Currency getCurrency(String currencyValue) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equalsIgnoreCase(currencyValue)) {
                return currency;
            }
        }
        return null;
    }

    public String formatPrice(double price) {
        BigInteger number = BigDecimal.valueOf(price).toBigInteger();
        //BigInteger number = new BigInteger(String.valueOf(price));
        Map.Entry<BigInteger, String> entry = MAP.floorEntry(number);
        if (entry == null) {
            return "0";
        }
        BigInteger key = entry.getKey();
        BigInteger d = key.divide(THOUSAND);
        BigInteger m = number.divide(d);
        float f = m.floatValue() / 1000.0f;
        float rounded = ((int) (f * 100.0)) / 100.0f;
        if (rounded % 1 == 0) {
            return ((int) rounded) + " " + entry.getValue();
        }
        return rounded + " " + entry.getValue();
    }

    public String formatPriceAsDollar(double price) {
        return "$" + formatPrice(price);
    }
}
