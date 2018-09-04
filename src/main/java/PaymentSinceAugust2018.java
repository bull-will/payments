public class PaymentSinceAugust2018 extends Payment {
    public PaymentSinceAugust2018(int year, int month, int electro_start, int electro_end, int water_start, int water_end) {
        super(year, month, electro_start, electro_end, water_start, water_end);
        super.waterTariff = 0.0;
    }


}
