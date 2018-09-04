public class Payment {

    static final String[] MONTHS = {"Без месяца", "Январь", "Февраль", "Март", "Апрель",
            "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};

    double electroTariff1 = 0.3084;
    int electroLimit1 = 75;
    double electroTariff2 = 0.6168;
    int electroLimit2 = 150;
    double electroTariff3 = 0.8388;
    int electroLimit3 = 800;
    double electroTariff4 = 2.6808;

    double heatingTariff = 540.0;
    double waterTariff = 14.0;
    double flatTariff = 197.0;
    double garbageTariff = 22.0;

    String name; //year and month represent the name for a payment

    int year;
    int month;

    int electro_start;
    int electro_end;
    int kWattConsumed;
    int water_start;
    int water_end;
    int m3consumed;

    int paymentForElectricity;
    int paymentForHeating;
    int paymentForWater;
    int paymentForFlat;
    int paymentForGarbage;
    int total;

    /*
    This constructor generates an instance of a monthly payment by the specified values:
    the number of the month (1 - 12), the start readings and the end readings of the electricity counter
    and the water counter.
    The methods of this class must be run to calculate the payments for all services and the total payment.
    Method payForEverything() (at the bottom) runs them all.
    */
    public Payment(int year, int month, int electro_start, int electro_end, int water_start, int water_end) {
        if (month >= 0 & month < 13) {
            this.year = year;
            this.month = month;
            this.electro_start = electro_start;
            this.electro_end = electro_end;
            this.water_start = water_start;
            this.water_end = water_end;
            name = year + "." + month + " (" + MONTHS[month] + ")";
            AdjustHeatingTariff();
        } else {
            throw new ArrayIndexOutOfBoundsException("Wrong number of month - Неправильный номер месяца");
        }

    }

    void payForElectricity() {
        kWattConsumed = electro_end - electro_start;
        double paymentForElectricityCopeek = 0;
        if (kWattConsumed >= 0) {
            if (kWattConsumed <= electroLimit1) {
                paymentForElectricityCopeek += kWattConsumed * electroTariff1;
            } else if (kWattConsumed <= electroLimit2) {
                paymentForElectricityCopeek += electroLimit1 * electroTariff1 +
                        (kWattConsumed - electroLimit1) * electroTariff2;
            } else if (kWattConsumed <= electroLimit3) {
                paymentForElectricityCopeek += electroLimit1 * electroTariff1 +
                        (electroLimit2 - electroLimit1) * electroTariff2 +
                        (kWattConsumed - electroLimit2) * electroTariff3;
            } else {
                paymentForElectricityCopeek += electroLimit1 * electroTariff1 +
                        (electroLimit2 - electroLimit1) * electroTariff2 +
                        (electroLimit3 - electroLimit2) * electroTariff3 +
                        (kWattConsumed - electroLimit3) * electroTariff4;

            }
        }
        paymentForElectricity = (int) Math.round(paymentForElectricityCopeek);
    }

    void payForHeating() {
        paymentForHeating = (int) Math.round(heatingTariff);
    }

    private void AdjustHeatingTariff() {
        if (month != 0 & month > 4 | month < 10) /* from november through march */ {
            heatingTariff = 0;
        } else if (month == 4 || month == 10) /* for half a month of heating in april and october */ {
            heatingTariff /= 2;
        }
    }

    void payForWater() {
        m3consumed = water_end - water_start;
        if (m3consumed >= 0) {
            paymentForWater = (int) Math.round(m3consumed * waterTariff);
        }
    }

    void payForFlat() {
        paymentForFlat = (int) Math.round(flatTariff);
    }

    void payForGarbage() {
        paymentForGarbage = (int) Math.round(garbageTariff);
    }

    void getTotal() {
        total = paymentForElectricity
                + paymentForHeating
                + paymentForWater
                + paymentForFlat
                + paymentForGarbage;
    }

    void payForEverything() {
        payForElectricity();
        payForHeating();
        payForWater();
        payForFlat();
        payForGarbage();
        getTotal();
    }

}

