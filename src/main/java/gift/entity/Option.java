package gift.entity;

import gift.exception.CustomException;
import gift.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "product_options")
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Option() {
    }

    public Option(String name, Long quantity, Product product) {
        this(null, name, quantity, product);
    }

    public Option(Long id, String name, Long quantity, Product product) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        product.addOption(this);
    }

    public void decreaseQuantity(Long orderQuantity) {
        if (!isQuantityEnough(orderQuantity)){
            throw new CustomException(ErrorCode.OptionNotEnough);
        }
        this.quantity = this.quantity - orderQuantity;
    }

    public void changeQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Boolean isQuantityEnough(Long orderQuantity) {
        return this.quantity - orderQuantity >= 0;
    }
}
