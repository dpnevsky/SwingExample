public class Furniture {

    private String article;
    private String description;
    private String price;

    public Furniture(){};

    public Furniture(String article, String description, String price) {
        this.setArticle(article);
        this.setDescription(description);
        this.setPrice(price);
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getArticle() {
        return article;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }
}