package uz.pdp.olchaProject;

import uz.pdp.olchaProject.model.Category;
import uz.pdp.olchaProject.model.User;
import uz.pdp.olchaProject.model.Product;
import uz.pdp.olchaProject.service.AuthenticationService;
import uz.pdp.olchaProject.service.CategoryService;
import uz.pdp.olchaProject.service.Lists;
import uz.pdp.olchaProject.service.ProductService;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {
    static AuthenticationService authenticationService = new AuthenticationService();
    static ProductService productService = new ProductService();
    static CategoryService categoryService = new CategoryService();

    static Scanner scannerInt = new Scanner(System.in);
    static Scanner scannerStr = new Scanner(System.in);
    static Scanner scannerBig = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n\n\t\t\t\uD83C\uDF52OLCHA.UZ\n");

            System.out.print("Are you a User or Admin !\n1 => User\n2 => Admin\n0 => STOP THE PROGRAM\n>>> ");
            switch (scannerInt.nextInt()) {
                case 1 -> user();
                case 2 -> admin();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Wrong choice please try again !!! ");
            }
        }
    }

    public static void admin(){
        while (true) {
            System.out.print("1 => Sign up\n2 => Sign in\n0 => Back\n>>> ");
            switch (scannerInt.nextInt()) {
                case 1 -> {
                    System.out.print("Insert phone number => +998");
                    String phoneNumber = scannerStr.nextLine();
                    if(!authenticationService.checkPhoneNumber(phoneNumber)){
                        System.out.println("Invalid phone number please try again");
                        continue;
                    }
                    int smsCode = authenticationService.sendSMS();
                    System.out.print("Sms code has been sent to your phone number.  "+smsCode+"\n>>>");
                    if(scannerInt.nextInt()!=smsCode){
                        System.out.println("Incorrect password please try again !!!");
                        break;
                    }
                    System.out.print("Insert your name => ");
                    String name = scannerStr.nextLine();
                    System.out.print("Insert password => ");
                    String password = scannerStr.nextLine();
                    User admin =authenticationService.signUp(phoneNumber, name, true, password);
                    System.out.println("You have successfully signed in! ");
                    System.out.println();
                    if (admin!=null&&admin.isAdmin()) {
                        adminMenu(admin);
                    } else {
                        System.out.println("Sorry we already have an account with this number !!! ");
                    }
                }
                case 2 -> {
                    System.out.print("Insert phone number => +998");
                    String phoneNumber = scannerStr.nextLine();
                    System.out.print("Insert password => ");
                    String password = scannerStr.nextLine();
                    User admin = authenticationService.signIn(phoneNumber, password);
                    if (admin != null) {
                        adminMenu(admin);
                    } else {
                        System.out.println("Sorry we could not find the account with this number !!! ");
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Wrong choice please try again !!! ");
            }
        }
    }

    public static void adminMenu(User admin){
        while (true) {
            System.out.print("1 => Create a category\n2 => Upload new product to market\n3 => List of products in market\n4 => List of sold products\n5 => Show Category\n6 => Delete a category\n0 => Back\n>>> ");
            switch (scannerInt.nextInt()) {
                case 1->{
                    createCategory();
                }
                case 2 -> {
                    uploadProduct(admin);
                }
                case 3 -> {
                    for (Product product : admin.bin) {
                        if(!product.isSaleState()) System.out.println(product.toString());
                    }
                }
                case 4 -> {
                    for (Product product : Lists.soldProductList) {
                        System.out.println(product.toStringSold());
                    }
                }
                case 5->{
                    for (Category category : categoryService.categories) {
                        System.out.println(category.toString());
                    }
                }
                case 6->{
                    for (Category category : categoryService.categories) {
                        System.out.println(category.toString());
                    }
                    System.out.print("Insert category Id: ");
                    int categoryId=scannerInt.nextInt();
                    if(categoryService.deleteCategory(categoryId)){
                        System.out.println("Category has been deleted successfully.");
                    }else {
                        System.out.println("Sorry something went wrong !!! Please try again");
                    }
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Wrong choice please try again !!! ");
            }
        }
    }

    public static void user(){
        while (true) {
            System.out.print("1 => Sing up\n2 => Sign in\n3 => Show categories\n0 => Back\n>>> ");
            switch (scannerInt.nextInt()) {
                case 1 -> {
                    System.out.print("Insert phone number => +998");
                    String phoneNumber = scannerStr.nextLine();
                    if(!authenticationService.checkPhoneNumber(phoneNumber)){
                        System.out.println("Invalid phone number please try again");
                        continue;
                    }
                    int smsCode = authenticationService.sendSMS();
                    System.out.print("Sms code has been sent to your phone number.  "+smsCode+"\n>>>");
                    if(scannerInt.nextInt()!=smsCode){
                        System.out.println("Incorrect password please try again !!!");
                        break;
                    }
                    System.out.print("Insert your name => ");
                    String name = scannerStr.nextLine();
                    System.out.print("Insert password => ");
                    String password = scannerStr.nextLine();
                    User user = authenticationService.signUp(phoneNumber, name, false, password);
                    System.out.println("You have successfully signed in! ");
                    System.out.println();
                    if (user!=null) {
                        userMenu(user);
                    } else {
                        System.out.println("Sorry we already have an account with this number !!! ");
                    }
                }
                case 2 -> {
                    System.out.print("Insert phone number => +998");
                    String phoneNumber = scannerStr.nextLine();
                    System.out.print("Insert password => ");
                    String password = scannerStr.nextLine();
                    User user = authenticationService.signIn(phoneNumber, password);
                    if (user != null&&!user.isAdmin()) {
                        userMenu(user);
                    } else {
                        System.out.println("Sorry we could not find the account with this number !!! ");
                    }

                }
                case 3 -> {
                    printCategory(null);
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Wrong choice please try again !!! ");
            }
        }
    }

    public static void userMenu(User user){
        while (true){
            System.out.print("1 => Show categories\n2 => Products bin\n0 => Back\n>>>");
            switch (scannerInt.nextInt()){
                case 1->{
                    printCategory(user);
                }
                case 2->{
                    for (Product product : user.bin) {
                        System.out.println(product.toString());
                    }
                }
                case 0->{
                    return;
                }
                default -> System.out.println("Wrong choice please try again !!! ");
            }
        }
    }

    public static void createCategory(){
        if(!categoryService.categories.isEmpty()){
            for (Category category : categoryService.categories) {
                if(category!=null) {
                    if(category.getParentCategoryID()==0){
                        System.out.println(category.getId() + " => " + category.getName());
                    }
                }
            }
            while (true) {
                System.out.print("\nChoose category's ID\n1 => Create category here\n0 => Back to main menu\n>>> ");
                int category = scannerInt.nextInt();
                if(category==0) {
                    return;
                }
                else if (category==1){
                    break;
                }
                else {
                    for (Category category1 : categoryService.categories) {
                        if (category1.getId() == category) {
                            if (!category1.productList.isEmpty()) {
                                System.out.println(category1.getId() + " => " + category1.getName() + " >>> There are products");
                            } else {
                                for (Category category2 : categoryService.categories) {
                                    if (category2.getParentCategoryID() == category) {
                                        System.out.println(category2.getId() + " => " + category2.getName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.print("Insert category name: ");
        String categoryName = scannerStr.nextLine();
        System.out.print("Insert category Id: ");
        int categoryId=scannerInt.nextInt();
        categoryService.addCategory(categoryName,categoryId);
    }

    public static void uploadProduct(User admin){
        if(categoryService.categories.isEmpty()){
            System.out.println("There is no any categories to upload new product to market !!!\nPlease create a category first");
            return;
        }
        for (Category category : categoryService.categories) {
            if(category!=null) {
                if(category.getParentCategoryID()==0){
                    System.out.println(category.getId() + " => " + category.getName());
                }
            }
        }
        while (true) {
            System.out.print("\nChoose category's ID\n1 => Upload product here\n0 => Back to main menu\n>>> ");
            int category = scannerInt.nextInt();
            if(category==0) {
                return;
            }
            else if (category==1){
                break;
            }
            else {
                for (Category category1 : categoryService.categories) {
                    if (category1.getId() == category) {
                        if (!category1.productList.isEmpty()) {
                            for (Product product : category1.productList) {
                                System.out.println("ID: "+product.getId()+"\nName: "+product.getName()+"\nPrice: "+product.getPrice()+"\n");
                            }
                        } else {
                            for (Category category2 : categoryService.categories) {
                                if (category2.getParentCategoryID() == category) {
                                    System.out.println(category2.getId() + " => " + category2.getName());
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.print("Insert name of product: ");
        String productName = scannerStr.nextLine();
        System.out.print("Insert category ID: ");
        int categoryID = scannerInt.nextInt();
        System.out.print("Insert the price: ");
        BigDecimal price = scannerBig.nextBigDecimal();
        Product product = productService.addProduct(productName, price,categoryID,categoryService);
        admin.bin.add(product);
        System.out.print("The product have been uploaded successfully\n");
    }

    public static void printCategory(User user){
        for (Category category : categoryService.categories) {
            if(category!=null) {
                if(category.getParentCategoryID()==0){
                    System.out.println(category.getId() + " => " + category.getName());
                }
            }
        }
        while (true) {
            System.out.print("\nChoose category's ID >> ");
            int category = scannerInt.nextInt();
            for (Category category1 : categoryService.categories) {
                if (category1.getId() == category) {
                    if (!category1.productList.isEmpty()) {
                        for (Product product : category1.productList) {
                            if (!product.isSaleState()) {
                                System.out.println("===========================================================");
                                System.out.println("ID => " + product.getId() + "\nName => " + product.getName() + "\nPrice => " + product.getPrice());
                                System.out.println("===========================================================");
                            }
                        }
                        System.out.print("1 => Buy a product\n0 => Back\n>>> ");
                        if (scannerInt.nextInt() == 1) {
                            if (user == null) {
                                System.out.println("\n!!!! whoops you did not sign in yet");
                                System.out.println("Do you have an account, if yes please sign in first, if not please sign up !!! \n");
                                return;
                            }
                            System.out.print("Choose ID of product which you wanna buy\n>>>");
                            int id = scannerInt.nextInt();
                            for (Product product : category1.productList) {
                                if (product.getId() == id) {
                                    product.setSoldByUser(user);
                                    user.bin.add(product);
                                    Lists.soldProductList.add(product);
                                    productService.setProductToSold(product);
                                    System.out.println("You ordered the product successfully");
                                    System.out.println("Your product will be delivered next 3 working days\n \t\t Thank you for your purchase");
                                    System.out.println("\n1 => Go back Main menu\n2 => Product list");
                                    if (scannerInt.nextInt() == 1) {
                                        return;
                                    } else {
                                        for (Category category5 : categoryService.categories) {
                                            if(category5!=null) {
                                                if(category5.getParentCategoryID()==0){
                                                    System.out.println(category5.getId() + " => " + category5.getName());
                                                }
                                            }
                                        }
                                        break;
                                    }
                                }
                            }

                        } else {
                            return;
                        }
                    } else {
                        for (Category category2 : categoryService.categories) {
                            if (category2.getParentCategoryID() == category) {
                                System.out.println(category2.getId() + " => " + category2.getName());
                            }
                        }
                    }
                }
            }
        }
    }
}