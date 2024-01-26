public class Customer {
    private String name;
    private String pin;

    public Customer(String name, String pin) {
        this.name = name;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
