package com.dreampany.lca.misc;

import android.content.Context;
import android.view.TextureView;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.dreampany.frame.util.TextUtil;
import com.dreampany.lca.R;
import com.dreampany.lca.api.cmc.enums.CmcCurrency;
import com.dreampany.lca.data.model.Currency;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

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
    private Set<Currency> cryptos;
    @NonNull
    private DecimalFormat cryptoFormatter;

    private static final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private static final String NAMES[] = new String[]{"T", "M", "B", "T"};
    private static final NavigableMap<BigInteger, String> MAP;
    private final Map<Currency, String> symbols;

    static {
        MAP = new TreeMap<>();
        for (int i = 0; i < NAMES.length; i++) {
            MAP.put(THOUSAND.pow(i + 1), NAMES[i]);
        }
    }

    @Inject
    public CurrencyFormatter(Context context) {
        this.context = context;
        formats = Maps.newConcurrentMap();
        cryptos = Sets.newHashSet(Currency.getCryptoCurrencies());
        cryptoFormatter = new DecimalFormat(context.getString(R.string.crypto_formatter));
        symbols = Maps.newConcurrentMap();
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

    public CmcCurrency getCurrency(String currencyValue) {
        for (CmcCurrency cmcCurrency : CmcCurrency.values()) {
            if (cmcCurrency.name().equalsIgnoreCase(currencyValue)) {
                return cmcCurrency;
            }
        }
        return null;
    }

    public String roundPrice(double price) {
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

    public String getSymbol(Currency currency) {
        if (!symbols.containsKey(currency)) {
            String symbol = null;
            if (currency.isCrypto()) {
                switch (currency) {
                    case BTC:
                        symbol = context.getString(R.string.btc_symbol);
                        break;
                    case ETH:
                        symbol = context.getString(R.string.eth_symbol);
                        break;
                    case LTC:
                        symbol = context.getString(R.string.ltc_symbol);
                        break;
                }
            } else {
                symbol = java.util.Currency.getInstance(currency.name()).getSymbol();
            }
            symbols.put(currency, symbol);
        }
        return symbols.get(currency);
    }

    public String roundPrice(double price, Currency currency) {
        String symbol = getSymbol(currency);
        String amount = roundPrice(price);
        return symbol + Constants.Sep.SPACE + amount;
    }

    public String formatPrice(double price, Currency currency) {
        int priceResId = getPriceResId(currency);
        return TextUtil.getString(context, priceResId, price);
    }

    public int getPriceResId(Currency currency) {
        int resId = 0;
        switch (currency) {
            case BRL:
                resId = R.string.brl_format;
                break;
            case CAD:
            case HKD:
            case SEK:
            case USD:
                resId = R.string.usd_format;
                break;
            case CHF:
                resId = R.string.chf_format;
                break;
            case CNY:
                resId = R.string.cny_format;
                break;
            case EUR:
                resId = R.string.euro_format;
                break;
            case INR:
                resId = R.string.inr_format;
                break;
            case ILS:
                resId = R.string.ils_format;
                break;
            case JPY:
                resId = R.string.jpy_format;
                break;
            case KRW:
                resId = R.string.krw_format;
                break;
            case TRY:
                resId = R.string.try_format;
                break;
            case ZAR:
                resId = R.string.zar_format;
                break;
        }
        return resId;
    }

}
