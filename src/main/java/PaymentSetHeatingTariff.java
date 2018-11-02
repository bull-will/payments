public class PaymentSetHeatingTariff extends Payment {
    double heatingMustPay = 0d;

    public PaymentSetHeatingTariff(int year, int month, int electro_start, int electro_end,
                                   int water_start, int water_end) {
        this(year, month, electro_start, electro_end, water_start, water_end, 0);
    }

    public PaymentSetHeatingTariff(int year, int month, int electro_start, int electro_end,
                                   int water_start, int water_end, double heatingMustPay) {
        super(year, month, electro_start, electro_end, water_start, water_end, heatingMustPay);
        heatingTariff = 270.11;
        payForEverything(); // no need to call it manually
    }

}
