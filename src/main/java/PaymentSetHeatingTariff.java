@Deprecated
/* Should deprecate since november*/
public class PaymentSetHeatingTariff extends Payment {
    double heatingMustPay = 0d;

    public PaymentSetHeatingTariff(int year, int month, int electro_start, int electro_end,
                                   int water_start, int water_end) {
        super(year, month, electro_start, electro_end, water_start, water_end);
        heatingTariff = 270.11;
    }


}
