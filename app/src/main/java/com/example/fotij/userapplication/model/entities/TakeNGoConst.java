package com.example.fotij.userapplication.model.entities;

/**
 * Created by fotij on 23/12/2017.
 */

import android.content.ContentValues;

import java.util.ArrayList;

public class TakeNGoConst {
    public static class ClientConst {
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String LASTNAME = "lastName";
        public static final String PHONE = "phoneNum";
        public static final String EMAILADDRESS = "emailAddress";
        public static final String CREDITCARDNUM = "creditCardNum";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }

    public static class Userconst {
        public static final String USERNAME = "_username";
        public static final String PASSWORD = "password";
    }

    public static class CarModelConst {
        public static final String CARMODELCODE = "_carModelCode";
        public static final String COMPANYNAME = "companyName";
        public static final String CARMODELNAME = "carModelName";
        public static final String ENGINEDISPLACMENT = "engineDisplacement";
        public static final String TRANSMISSION = "transmission";
        public static final String PASSENGERS = "passengers";
        public static final String LUGGAGE = "luggage";
        public static final String AC = "ac";
        public static final String IMAGEURL = "imgURL";
        public static final String INUSE = "inUse";
        public static final String BYTEARRAY = "byteArray";
    }

    public static class BranchConst {
        public static final String ID = "_branchNum";
        public static final String NAME = "address";
        public static final String PARKINGSPOTSNUM = "parkingSpotsNum";
        public static final String IMAGEURL = "imgURL";
        public static final String BRANCHREVENUE = "branchRevenue";
        public static final String ESTABLISHEDDATE = "MyDate";
        public static final String INUSE = "inUse";
        public static final String CARIDSLIST = "carIdsList";

    }

    public static class BranchImageConst {
        public static final String ID = "_branchNum";
        public static final String IMAGEURL = "imgURL";
    }

    public static class CarConst {
        public static final String CARNUM = "_carNum";
        public static final String BRANCHNUM = "branchNum";
        public static final String CARMODEL = "carModel";
        public static final String MILEAGE = "mileage";
        public static final String RATING = "rating";
        public static final String NUMOFRATINGS = "numOfRatings";
        public static final String ONEDAYCOST = "oneDayCost";
        public static final String ONEKILOMETERCOST = "oneKilometerCost";
        public static final String IMAGEURL = "imgURL";
        public static final String YEAR = "year";
        public static final String INUSE = "inUse";
    }

    public static class OrderConst {
        public static final String ORDERNUMBER = "_orderNumber";
        public static final String CLIENTNUM = "clientNum";
        public static final String ORDEROPEN = "orderOpen";
        public static final String CARNUM = "carNum";
        public static final String DATESTART = "dateStart";
        public static final String DATEEND = "dateEnd";
        public static final String MILEAGESTART = "mileageStart";
        public static final String MILEAGEEND = "mileageEnd";
        public static final String GASREFILL = "gasRefill";
        public static final String LITERSFILLED = "litersFilled";
        public static final String PAYMENT = "payment";
    }

    public static ContentValues OrderToContentValues(Order order, boolean add_or_open) {
        ContentValues contentValues = new ContentValues();
        if (add_or_open)
            contentValues.put(OrderConst.ORDERNUMBER, order.getOrderNumber());
        contentValues.put(OrderConst.CLIENTNUM, order.getClientNum());
        int isopenInt;
        if (order.isOrderOpen()) isopenInt = 1;
        else
            isopenInt = 0;
        contentValues.put(OrderConst.ORDEROPEN, isopenInt);
        contentValues.put(OrderConst.CARNUM, order.getCarNum());
        contentValues.put(OrderConst.DATESTART, order.getDateStart());
        contentValues.put(OrderConst.DATEEND, order.getDateEnd());
        contentValues.put(OrderConst.MILEAGESTART, order.getMileageStart());
        contentValues.put(OrderConst.MILEAGEEND, order.getMileageEnd());
        int isgassInt;
        if (order.isGasRefill()) isgassInt = 1;
        else
            isgassInt = 0;
        contentValues.put(OrderConst.GASREFILL, isgassInt);
        contentValues.put(OrderConst.LITERSFILLED, order.getLitersFilled());
        contentValues.put(OrderConst.PAYMENT, order.getPayment());
        return contentValues;
    }

    public static Order ContentValuesToOrder(ContentValues contentValues) {
        Order order = new Order();
        boolean open;
        if (contentValues.getAsInteger(OrderConst.ORDEROPEN) == 1) open = true;
        else
            open = false;
        order.setOrderNumber(contentValues.getAsInteger(OrderConst.ORDERNUMBER));
        order.setClientNum(contentValues.getAsInteger(OrderConst.CLIENTNUM));
        order.setOrderOpen(open);
        order.setCarNum(contentValues.getAsInteger(OrderConst.CARNUM));
        order.setDateStart(contentValues.getAsString(OrderConst.DATESTART));
        order.setDateEnd((contentValues.getAsString(OrderConst.DATEEND)));
        order.setMileageStart((contentValues.getAsDouble(OrderConst.MILEAGESTART)));
        order.setMileageEnd((contentValues.getAsDouble(OrderConst.MILEAGEEND)));
        boolean gas;
        if (contentValues.getAsInteger(OrderConst.GASREFILL) == 1)
            gas = true;
        else gas = false;
        order.setGasRefill((gas));
        order.setLitersFilled((contentValues.getAsInteger(OrderConst.LITERSFILLED)));
        order.setPayment((contentValues.getAsInteger(OrderConst.PAYMENT)));
        return order;
    }


    public static ContentValues CarToContentValues(Car car) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarConst.CARNUM, car.getCarNum());
        contentValues.put(CarConst.BRANCHNUM, car.getBranchNum());
        contentValues.put(CarConst.CARMODEL, car.getCarModel());
        contentValues.put(CarConst.MILEAGE, car.getMileage());
        contentValues.put(CarConst.RATING, car.getRating());
        contentValues.put(CarConst.NUMOFRATINGS, car.getNumOfRatings());
        contentValues.put(CarConst.ONEDAYCOST, car.getOneDayCost());
        contentValues.put(CarConst.ONEKILOMETERCOST, car.getOneKilometerCost());
        contentValues.put(CarConst.IMAGEURL, car.getImgURL());
        contentValues.put(CarConst.YEAR, car.getYear());
        if (car.isInUse()) contentValues.put(CarConst.INUSE, 1);
        else contentValues.put(CarConst.INUSE, 0);
        //contentValues.put(CarConst.INUSE, car.isInUse());
        return contentValues;
    }

    public static Car ContentValuesToCar(ContentValues contentValues) {
        Car car = new Car();
        car.setCarNum(contentValues.getAsInteger(CarConst.CARNUM));
        car.setBranchNum(contentValues.getAsInteger(CarConst.BRANCHNUM));
        car.setCarModel(contentValues.getAsInteger(CarConst.CARMODEL));
        car.setMileage(contentValues.getAsDouble(CarConst.MILEAGE));
        car.setRating(contentValues.getAsDouble(CarConst.RATING));
        car.setNumOfRatings((contentValues.getAsInteger(CarConst.NUMOFRATINGS)));
        car.setOneDayCost((contentValues.getAsDouble(CarConst.ONEDAYCOST)));
        car.setOneKilometerCost((contentValues.getAsDouble(CarConst.ONEKILOMETERCOST)));
        car.setImgURL((contentValues.getAsString(CarConst.IMAGEURL)));
        car.setYear((contentValues.getAsInteger(CarConst.YEAR)));
        if (contentValues.getAsInteger(CarConst.INUSE) == 1) car.setInUse(true);
        else car.setInUse(false);
        //car.setInUse((contentValues.getAsBoolean(CarConst.INUSE)));
        return car;
    }

    public static ContentValues ClientToContentValues(Client client) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientConst.ID, client.getId());
        contentValues.put(ClientConst.NAME, client.getName());
        contentValues.put(ClientConst.LASTNAME, client.getLastName());
        contentValues.put(ClientConst.PHONE, client.getPhoneNum());
        contentValues.put(ClientConst.EMAILADDRESS, client.getEmailAddress());
        contentValues.put(ClientConst.CREDITCARDNUM, client.getCreditCardNum());
        contentValues.put(ClientConst.USERNAME, convertUsernameToLong(client.getUsername()));
        return contentValues;
    }

    public static Client ContentValuesToClient(ContentValues contentValues) {
        Client client = new Client();
        client.setId(contentValues.getAsInteger(ClientConst.ID));
        client.setName(contentValues.getAsString(ClientConst.NAME));
        client.setLastName(contentValues.getAsString(ClientConst.LASTNAME));
        client.setPhoneNum(contentValues.getAsString(ClientConst.PHONE));
        client.setEmailAddress(contentValues.getAsString(ClientConst.EMAILADDRESS));
        client.setCreditCardNum((contentValues.getAsString(ClientConst.CREDITCARDNUM)));
        client.setUsername(convertLongToUsername(contentValues.getAsLong(ClientConst.USERNAME)));
        return client;
    }

    public static ContentValues UserToContentValues(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Userconst.USERNAME, convertUsernameToLong(user.getUsername()));
        contentValues.put(Userconst.PASSWORD, user.getPassword());
        return contentValues;
    }

    public static User ContentValuesToUser(ContentValues contentValues) {
        User user = new User();
        user.setUsername(convertLongToUsername(contentValues.getAsLong(Userconst.USERNAME)));
        user.setPassword((contentValues.getAsString(Userconst.PASSWORD)));
        return user;
    }

    public static ContentValues CarModelToContentValues(CarModel carModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarModelConst.CARMODELCODE, carModel.getCarModelCode());
        contentValues.put(CarModelConst.COMPANYNAME, carModel.getCompanyName());
        contentValues.put(CarModelConst.CARMODELNAME, carModel.getCarModelName());
        contentValues.put(CarModelConst.ENGINEDISPLACMENT, carModel.getEngineDisplacement());
        contentValues.put(CarModelConst.TRANSMISSION, String.valueOf(carModel.getTransmission()));
        contentValues.put(CarModelConst.PASSENGERS, carModel.getPassengers());
        contentValues.put(CarModelConst.LUGGAGE, carModel.getLuggage());
        if (carModel.isAc()) contentValues.put(CarModelConst.AC, 1);
        else contentValues.put(CarModelConst.AC, 0);
        //contentValues.put(CarModelConst.AC, carModel.isAc());

        if (carModel.isInUse()) contentValues.put(CarModelConst.INUSE, 1);
        else contentValues.put(CarModelConst.INUSE, 0);
        //contentValues.put(CarModelConst.INUSE, carModel.isInUse());
        contentValues.put(CarModelConst.IMAGEURL, carModel.getImgURL());
        return contentValues;
    }

    public static CarModel ContentValuesToCarModel(ContentValues contentValues) {
        CarModel carModel = new CarModel();
        carModel.setCarModelCode(contentValues.getAsInteger(CarModelConst.CARMODELCODE));
        carModel.setCompanyName(contentValues.getAsString(CarModelConst.COMPANYNAME));
        carModel.setCarModelName(contentValues.getAsString(CarModelConst.CARMODELNAME));
        carModel.setEngineDisplacement(contentValues.getAsDouble(CarModelConst.ENGINEDISPLACMENT));
        carModel.setTransmission(Transmission.valueOf(contentValues.getAsString(CarModelConst.TRANSMISSION)));
        carModel.setPassengers(contentValues.getAsInteger(CarModelConst.PASSENGERS));
        carModel.setLuggage((contentValues.getAsInteger(CarModelConst.LUGGAGE)));

        if (contentValues.getAsInteger(CarModelConst.AC) == 1) carModel.setAc(true);
        else carModel.setAc(false);
        if (contentValues.getAsInteger(CarModelConst.INUSE) == 1) carModel.setInUse(true);
        else carModel.setInUse(false);
        //carModel.setAc(contentValues.getAsBoolean(CarModelConst.AC));
        carModel.setImgURL(contentValues.getAsString(CarModelConst.IMAGEURL));
        //carModel.setInUse(contentValues.getAsBoolean(CarModelConst.INUSE));
        return carModel;
    }

    public static ContentValues BranchToContentValues(Branch branch) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BranchConst.ID, branch.getBranchNum());
        contentValues.put(BranchConst.NAME, branch.getMyAddress().toString());
        contentValues.put(BranchConst.PARKINGSPOTSNUM, branch.getParkingSpotsNum());
        contentValues.put(BranchConst.IMAGEURL, branch.getImgURL());
        contentValues.put(BranchConst.BRANCHREVENUE, branch.getBranchRevenue());
        contentValues.put(BranchConst.ESTABLISHEDDATE, branch.getEstablishedDate().saveDate());
        if (branch.isInUse()) contentValues.put(BranchConst.INUSE, 1);
        else contentValues.put(BranchConst.INUSE, 0);
        //contentValues.put(BranchConst.INUSE, branch.isInUse());
        contentValues.put(BranchConst.CARIDSLIST, branch.convertCarIDtoString());
        return contentValues;
    }

    public static Branch ContentValuesToBranch(ContentValues contentValues) {
        Branch branch = new Branch();
        branch.setBranchNum(contentValues.getAsInteger(BranchConst.ID));
        branch.setMyAddress(getAddressFromString(contentValues.getAsString(BranchConst.NAME)));
        branch.setParkingSpotsNum(contentValues.getAsInteger(BranchConst.PARKINGSPOTSNUM));
        branch.setImgURL(contentValues.getAsString(BranchConst.IMAGEURL));
        branch.setBranchRevenue(contentValues.getAsDouble(BranchConst.BRANCHREVENUE));
        branch.setEstablishedDate(getDateFromString(contentValues.getAsString(BranchConst.ESTABLISHEDDATE)));
        if (contentValues.getAsInteger(BranchConst.INUSE) == 1) branch.setInUse(true);
        else branch.setInUse(false);
        //branch.setInUse(contentValues.getAsBoolean(BranchConst.INUSE));
        branch.setCarIds(getCarsFromString((contentValues.getAsString(BranchConst.CARIDSLIST))));
        return branch;
    }


    public static ContentValues ClientIdToContentValues(int clientID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientConst.ID, clientID);
        return contentValues;
    }

    public static ContentValues CarModelIdToContentValues(int carModelID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarModelConst.CARMODELCODE, carModelID);
        return contentValues;
    }

    public static ContentValues CarIdToContentValues(int carID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarConst.CARNUM, carID);
        return contentValues;
    }

    public static ContentValues CarId_ModelId_BranchId_ToContentValues(int carID, int carModelID, int branchID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarConst.CARNUM, carID);
        contentValues.put(CarConst.CARMODEL, carModelID);
        contentValues.put(CarConst.BRANCHNUM, branchID);
        return contentValues;
    }

    public static ContentValues BranchIdToContentValues(int branchID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BranchConst.ID, branchID);
        return contentValues;
    }

    public static ContentValues UserIdToContentValues(String username) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Userconst.USERNAME, convertUsernameToLong(username));
        return contentValues;
    }

    public static ContentValues OrderClientToContentValues(int clientNum) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OrderConst.CLIENTNUM, clientNum);
        return contentValues;
    }


    public static ContentValues BranchImageIdToContentValues(int branchID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BranchConst.ID, branchID);
        return contentValues;
    }

    public static ContentValues CarModelImageIdToContentValues(int carModelID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CarModelConst.CARMODELCODE, carModelID);
        return contentValues;
    }

    public static MyAddress getAddressFromString(String address) {
        MyAddress myAddress = new MyAddress();
        String[] list = address.split("~~");
        myAddress.setAddressName(list[0]);
        myAddress.setCountry(list[1]);
        myAddress.setLocality(list[2]);
        myAddress.setLatitude(Double.parseDouble(list[3]));
        myAddress.setLongitude(Double.parseDouble(list[4]));
        return myAddress;
    }

    public static MyDate getDateFromString(String date) {
        MyDate myDate = new MyDate();
        String[] list = date.split("~~");
        myDate.setYear(Integer.parseInt(list[0]));
        myDate.setMonth(list[1]);
        myDate.setDay(Integer.parseInt(list[2]));
        return myDate;
    }

    public static ArrayList<Integer> getCarsFromString(String cars) {
        ArrayList<Integer> carids = new ArrayList<>();
        if (cars.equals("")) return carids;
        String[] list = cars.split("~~");
        for (int i = 0; i < list.length; i++) {
            carids.add(Integer.valueOf(list[i]));
        }
        return carids;
    }


    public static long convertUsernameToLong(String username) {
        long usernameNum = 0;
        for (int i = 0; i < username.length(); i++) {
            switch (username.toLowerCase().charAt(i)) {
                case 'a': {
                    usernameNum += 10;
                    usernameNum *= 100;
                    break;
                }
                case 'b': {
                    usernameNum += 11;
                    usernameNum *= 100;
                    break;
                }
                case 'c': {
                    usernameNum += 12;
                    usernameNum *= 100;
                    break;
                }
                case 'd': {
                    usernameNum += 13;
                    usernameNum *= 100;
                    break;
                }
                case 'e': {
                    usernameNum += 14;
                    usernameNum *= 100;
                    break;
                }
                case 'f': {
                    usernameNum += 15;
                    usernameNum *= 100;
                    break;
                }
                case 'g': {
                    usernameNum += 16;
                    usernameNum *= 100;
                    break;
                }
                case 'h': {
                    usernameNum += 17;
                    usernameNum *= 100;
                    break;
                }
                case 'i': {
                    usernameNum += 18;
                    usernameNum *= 100;
                    break;
                }
                case 'j': {
                    usernameNum += 19;
                    usernameNum *= 100;
                    break;
                }
                case 'k': {
                    usernameNum += 20;
                    usernameNum *= 100;
                    break;
                }
                case 'l': {
                    usernameNum += 21;
                    usernameNum *= 100;
                    break;
                }
                case 'm': {
                    usernameNum += 22;
                    usernameNum *= 100;
                    break;
                }
                case 'n': {
                    usernameNum += 23;
                    usernameNum *= 100;
                    break;
                }
                case 'o': {
                    usernameNum += 24;
                    usernameNum *= 100;
                    break;
                }
                case 'p': {
                    usernameNum += 25;
                    usernameNum *= 100;
                    break;
                }
                case 'q': {
                    usernameNum += 26;
                    usernameNum *= 100;
                    break;
                }
                case 'r': {
                    usernameNum += 27;
                    usernameNum *= 100;
                    break;
                }
                case 's': {
                    usernameNum += 28;
                    usernameNum *= 100;
                    break;
                }
                case 't': {
                    usernameNum += 29;
                    usernameNum *= 100;
                    break;
                }
                case 'u': {
                    usernameNum += 30;
                    usernameNum *= 100;
                    break;
                }
                case 'v': {
                    usernameNum += 31;
                    usernameNum *= 100;
                    break;
                }
                case 'w': {
                    usernameNum += 32;
                    usernameNum *= 100;
                    break;
                }
                case 'x': {
                    usernameNum += 33;
                    usernameNum *= 100;
                    break;
                }
                case 'y': {
                    usernameNum += 34;
                    usernameNum *= 100;
                    break;
                }
                case 'z': {
                    usernameNum += 35;
                    usernameNum *= 100;
                    break;
                }
                case '0': {
                    usernameNum += 36;
                    usernameNum *= 100;
                    break;
                }
                case '1': {
                    usernameNum += 37;
                    usernameNum *= 100;
                    break;
                }
                case '2': {
                    usernameNum += 38;
                    usernameNum *= 100;
                    break;
                }
                case '3': {
                    usernameNum += 39;
                    usernameNum *= 100;
                    break;
                }
                case '4': {
                    usernameNum += 40;
                    usernameNum *= 100;
                    break;
                }
                case '5': {
                    usernameNum += 41;
                    usernameNum *= 100;
                    break;
                }
                case '6': {
                    usernameNum += 42;
                    usernameNum *= 100;
                    break;
                }
                case '7': {
                    usernameNum += 43;
                    usernameNum *= 100;
                    break;
                }
                case '8': {
                    usernameNum += 44;
                    usernameNum *= 100;
                    break;
                }
                case '9': {
                    usernameNum += 45;
                    usernameNum *= 100;
                    break;
                }
            }
        }
        return usernameNum;
    }

    public static String convertLongToUsername(long usernameNum) {
        String username = "";
        long tmp = usernameNum;
        while (tmp > 0) {
            int letter = (int) (tmp % 100);
            tmp /= 100;
            switch (letter) {
                case 10: {
                    username = 'a' + username;
                    break;
                }
                case 11: {
                    username = 'b' + username;
                    break;
                }
                case 12: {
                    username = 'c' + username;
                    break;
                }
                case 13: {
                    username = 'd' + username;
                    break;
                }
                case 14: {
                    username = 'e' + username;
                    break;
                }
                case 15: {
                    username = 'f' + username;
                    break;
                }
                case 16: {
                    username = 'g' + username;
                    break;
                }
                case 17: {
                    username = 'h' + username;
                    break;
                }
                case 18: {
                    username = 'i' + username;
                    break;
                }
                case 19: {
                    username = 'j' + username;
                    break;
                }
                case 20: {
                    username = 'k' + username;
                    break;
                }
                case 21: {
                    username = 'l' + username;
                    break;
                }
                case 22: {
                    username = 'm' + username;
                    break;
                }
                case 23: {
                    username = 'n' + username;
                    break;
                }
                case 24: {
                    username = 'o' + username;
                    break;
                }
                case 25: {
                    username = 'p' + username;
                    break;
                }
                case 26: {
                    username = 'q' + username;
                    break;
                }
                case 27: {
                    username = 'r' + username;
                    break;
                }
                case 28: {
                    username = 's' + username;
                    break;
                }
                case 29: {
                    username = 't' + username;
                    break;
                }
                case 30: {
                    username = 'u' + username;
                    break;
                }
                case 31: {
                    username = 'v' + username;
                    break;
                }
                case 32: {
                    username = 'w' + username;
                    break;
                }
                case 33: {
                    username = 'x' + username;
                    break;
                }
                case 34: {
                    username = 'y' + username;
                    break;
                }
                case 35: {
                    username = 'z' + username;
                    break;
                }
                case 36: {
                    username = '0' + username;
                    break;
                }
                case 37: {
                    username = '1' + username;
                    break;
                }
                case 38: {
                    username = '2' + username;
                    break;
                }
                case 39: {
                    username = '3' + username;
                    break;
                }
                case 40: {
                    username = '4' + username;
                    break;
                }
                case 41: {
                    username = '5' + username;
                    break;
                }
                case 42: {
                    username = '6' + username;
                    break;
                }
                case 43: {
                    username = '7' + username;
                    break;
                }
                case 44: {
                    username = '8' + username;
                    break;
                }
                case 45: {
                    username = '9' + username;
                    break;
                }
            }
        }
        return username;
    }


}

