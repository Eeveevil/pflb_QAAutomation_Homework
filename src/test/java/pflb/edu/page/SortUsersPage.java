package pflb.edu.page;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class SortUsersPage extends AbstractPage {
//    private WebDriver driver;
//    private WebDriverWait wait;
//    private WebElement sortById;
//    private WebElement sortByFirstName;
//    private WebElement sortByLastName;
//    private WebElement sortByAge;
//    private WebElement sortBySex;
//    private WebElement sortByMoney;


    public SortUsersPage(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }
//        this.driver = driver;
//        this.wait = wait;
//        sortById = driver.findElement(By.xpath("//button[contains(text(),'ID')]"));
//        sortByFirstName = driver.findElement(By.xpath("//button[contains(text(),'First') and contains(text(),'Name')]"));
//        sortByLastName = driver.findElement(By.xpath("//button[contains(text(),'Last') and contains(text(),'Name')]"));
//        sortByAge = driver.findElement(By.xpath("//button[contains(text(),'Age')]"));
//        sortBySex = driver.findElement(By.xpath("//button[contains(text(),'Sex')]"));
//        sortByMoney = driver.findElement(By.xpath("//button[contains(text(),'Money')]"));
//}

//    private Alert findAlert() {
//        return wait.until(drv -> drv.switchTo().alert());
//    }

    @FindBy
    (css = "table tbody tr")
    private List<WebElement> userRows;

    private  static final int ID_COL = 0;

//    public void SortByIdButton(){sortById.click();}

    public void SortByIdButton(boolean state)
    {
        var idColumn = getSortButton("ID");
        try { Thread.sleep(5000); }
        catch(InterruptedException ie) {}
        if(state)
            idColumn.click();
        else
        {
            idColumn.click();
            idColumn.click();
        }
    }


//    public void SortByFirstNameButton(){sortByFirstName.click();}
//    public void SortByLastNameButton(){sortByLastName.click();}
//    public void SortByAgeButton(){sortByAge.click();}
//    public void SortBySexButton(){sortBySex.click();}
//    public void SortByMoneyButton(){sortByMoney.click();}

//    private void topCatch(){
//            driver.get(String.valueOf(By.xpath("//table[@class='table table-striped table-bordered table-hover']//tbody//tr//td")));
//    }
//
//    private void underTopCatch(){
//        driver.get(String.valueOf(By.xpath("//table[@class='table table-striped table-bordered table-hover']//tbody//tr//td")));
//    }


    public boolean checkSortedUsers(boolean state)
    {
        boolean check;
        var numbers = this.getListUsersId();
        var minId =  Arrays.stream(numbers).min().getAsLong();
        var maxId = Arrays.stream(numbers).max().getAsLong();

        var userFirst = getUserIdByRowNum(0);
        var userLast = getUserIdByRowNum(userRows.size() - 1);

        if(state)
            check = userFirst < userLast && userFirst == minId && userLast == maxId;
        else
            check = userFirst > userLast && userFirst != minId && userLast != maxId;

        return check;
    }

    public void cancelSorting(String buttonName)
    {
        while(true)
        {
            var btn = this.getSortButton(buttonName);
            var text = btn.getText().trim();
            if(text.length() != buttonName.length())
                btn.click();
            else
                return;
        }
    }

    private WebElement getSortButton(String buttonName)
    {
        var buttons = driver.findElements(By.tagName("button"));
        var button = buttons.stream().filter((opt) -> opt.getText().contains(buttonName)).findFirst().orElse(null);
        return button;
    }

    private List<WebElement> getUserRowCells(int num)
    {
        WebElement tableRow = userRows.get(num);
        return tableRow.findElements(By.cssSelector("td"));
    }
    public long[] getListUsersId()
    {
        long[] usersIds = new long[userRows.size()];
        String query = "http://77.50.236.203:4879/users"; //запрос на получение users
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(query).openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(250);
            connection.setReadTimeout(250);
            connection.connect();

            StringBuilder sb = new StringBuilder();
            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "cp1251"));
                String line;
                while((line = in.readLine()) != null)
                    sb.append(line);

                Object obj = new JSONParser().parse(sb.toString());
                JSONArray jsonArray = (JSONArray) obj;

                for(int i = 0; i < jsonArray.size(); i++)
                {
                    var jsonObj = (JSONObject) jsonArray.get(i);
                    usersIds[i]= (long) jsonObj.get("id");
                }

            } else {
                System.out.println("fail: " + connection.getResponseCode() + ", " + connection.getResponseMessage());
            }
        } catch(Throwable cause) {
            cause.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return usersIds;
    }

    public int getUserIdByRowNum(int num)
    {
        List<WebElement> tds = getUserRowCells(num);
        return Integer.parseInt(tds.get(ID_COL).getText());
    }


    @Deprecated
    public String getAlertTextThenDismiss(){
        return wait.until(drv -> {
            Alert alert = drv.switchTo().alert();
            String text = alert.getText();
            alert.dismiss();
            return text;
        });
    }
}
