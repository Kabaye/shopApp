package com.netcracker.edu.kulich.entity;

import com.netcracker.edu.kulich.utils.LocalDateAttributeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "date", nullable = false)
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date;

    @OneToMany(mappedBy = "order", orphanRemoval = true,
            cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne(optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Transient
    private double totalPrice;

    @Transient
    private int amountOfOrderItems;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private OrderStatusEnum orderStatus = OrderStatusEnum.IN_PROCESS;

    @Column(name = "paid_status", nullable = false)
    @Enumerated(value = EnumType.ORDINAL)
    private OrderPaymentStatusEnum orderPaymentStatus;

    @PostPersist
    @PostUpdate
    @PostLoad
    public void postPersistAndUpdate() {
        this.amountOfOrderItems = this.orderItems.size();
        totalPrice = 0;
        this.orderItems.stream().filter(Objects::nonNull).forEach(orderItem -> totalPrice += orderItem.getPrice());
    }

    public boolean addOffer(OrderItem orderItem) {
        return orderItems.add(orderItem);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("date=" + date)
                .add("customer=" + customer)
                .add("orderItems=" + orderItems)
                .add("totalPrice=" + totalPrice)
                .add("amountOfOrderItems=" + amountOfOrderItems)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (Double.compare(order.totalPrice, totalPrice) != 0) return false;
        if (amountOfOrderItems != order.amountOfOrderItems) return false;
        if (date != null ? !date.equals(order.date) : order.date != null) return false;
        if (!orderItems.equals(order.orderItems)) return false;
        if (customer != null ? !customer.equals(order.customer) : order.customer != null) return false;
        if (orderStatus != order.orderStatus) return false;
        return orderPaymentStatus == order.orderPaymentStatus;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (id ^ (id >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + orderItems.hashCode();
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        temp = Double.doubleToLongBits(totalPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + amountOfOrderItems;
        result = 31 * result + (orderStatus != null ? orderStatus.hashCode() : 0);
        result = 31 * result + (orderPaymentStatus != null ? orderPaymentStatus.hashCode() : 0);
        return result;
    }

    public void allNamesFixing() {
        customer.nameFixing();
        for (OrderItem item : orderItems) {
            item.nameFixing();
        }
    }
}
