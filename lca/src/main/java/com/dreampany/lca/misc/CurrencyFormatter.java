package com.dreampany.lca.misc;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.dreampany.lca.R;
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.data.model.Currency;
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
    private final Map<String, String> formats;
    @NonNull
    private Set<CmcCurrency> cryptos;
    @NonNull
    private DecimalFormat cryptoFormatter;

    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final String NAMES[] = new String[]{"T", "M", "B", "T"};
    private static final NavigableMap<BigInteger, String> MAP;
   // private final Map<Currency, String> formats

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
        cryptos = Sets.newHashSet(CmcCurrency.getCryptoCurrencies());
        cryptoFormatter = new DecimalFormat(context.getString(R.string.crypto_formatter));

        loadFormats();
    }

    private void loadFormats() {
        formats.put(Currency.BTC.name(), context.getString(R.string.btc_format));
        formats.put(Currency.ETH.name(), context.getString(R.string.eth_format));
        formats.put(Currency.LTC.name(), context.getString(R.string.ltc_format));

        formats.put(Currency.AUD.name(), getString(R.string.aud_format));
        formats.put(Currency.BRL.name(), getString(R.string.brl_format));
        formats.put(Currency.CAD.name(), getString(R.string.usd_format));
        formats.put(Currency.CHF.name(), getString(R.string.chf_format));
        formats.put(Currency.CLP.name(), getString(R.string.usd_format));
        formats.put(Currency.CNY.name(), getString(R.string.cny_format));
        formats.put(Currency.CZK.name(), getString(R.string.czk_format));
        formats.put(Currency.DKK.name(), getString(R.string.dkk_format));
        formats.put(Currency.GBP.name(), getString(R.string.gbp_format));
        formats.put(Currency.USD.name(), getString(R.string.usd_format));
    }

    @NonNull
    private String getString(@StringRes int resId) {
        return context.getString(resId);
    }

    public String getCryptoString(double price) {
        return cryptoFormatter.format(price);
    }

    public String format(CmcCurrency cmcCurrency, double price) {
        if (cryptos.contains(cmcCurrency)) {
            String priceValue = getCryptoString(price);
            return String.format(formats.get(cmcCurrency), priceValue);
        }
        String format = formats.get(cmcCurrency);
        if (format != null) {
            return String.format(format, price);
        }
        NumberFormat nf = NumberFormat.getInstance(context.getResources().getConfiguration().locale);
        nf.setMaximumFractionDigits(10);
        return cmcCurrency.name() + " " + nf.format(price);
    }

    public String format(String currencyValue, double price) {
        NumberFormat nf = NumberFormat.getInstance(context.getResources().getConfiguration().locale);
        nf.setMaximumFractionDigits(10);
        return currencyValue + " " + nf.format(price);
    }

    public CmcCurrency getCurrency(String currencyValue) {
        for (CmcCurrency cmcCurrency : CmcCurrency.values()) {
            if (cmcCurrency.name().equalsIgnoreCase(currencyValue)) {
                return cmcCurrency;
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

    public String formatPrice(double price, Currency currency) {
        return "";
    }


}
