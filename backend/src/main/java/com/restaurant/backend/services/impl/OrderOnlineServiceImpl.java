package com.restaurant.backend.services.impl;

import com.restaurant.backend.domains.dto.OrderOnline.OrderOnline;
import com.restaurant.backend.domains.dto.OrderOnline.OrderOnlineDetails;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDTO;
import com.restaurant.backend.domains.dto.OrderOnline.dto.OrderOnlineDetailsDTO;
import com.restaurant.backend.domains.dto.OrderOnline.enums.OrderStatus;
import com.restaurant.backend.domains.dto.OrderOnline.mapper.OrderOnlineMapper;
import com.restaurant.backend.domains.dto.OrderOnline.mapper.OrderOnlineDetailsMapper;
import com.restaurant.backend.domains.entities.Employee;
import com.restaurant.backend.repositories.EmployeeRepository;
import com.restaurant.backend.repositories.OrderOnlineRepository;
import com.restaurant.backend.repositories.OrderOnlineDetailsRepository;
import com.restaurant.backend.repositories.MenuItemRepository;
import com.restaurant.backend.services.OrderOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderOnlineServiceImpl implements OrderOnlineService {

    private final OrderOnlineRepository orderOnlineRepository;
    private final OrderOnlineDetailsRepository orderOnlineDetailsRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderOnlineMapper orderOnlineMapper;
    private final OrderOnlineDetailsMapper orderOnlineDetailsMapper;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public OrderOnlineServiceImpl(
            OrderOnlineRepository orderOnlineRepository,
            OrderOnlineDetailsRepository orderOnlineDetailsRepository,
            MenuItemRepository menuItemRepository,
            OrderOnlineMapper orderOnlineMapper,
            OrderOnlineDetailsMapper orderOnlineDetailsMapper,
            EmployeeRepository employeeRepository) {
        this.orderOnlineRepository = orderOnlineRepository;
        this.orderOnlineDetailsRepository = orderOnlineDetailsRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderOnlineMapper = orderOnlineMapper;
        this.orderOnlineDetailsMapper = orderOnlineDetailsMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public OrderOnlineDTO createOrder(OrderOnlineDTO orderOnlineDTO) {
        OrderOnline orderOnline = orderOnlineMapper.toEntity(orderOnlineDTO);

        // Set fields that might not be fully handled by default mapping or need explicit setting
        orderOnline.setUserId(orderOnlineDTO.getUserId());
        orderOnline.setPaymentMethod(orderOnlineDTO.getPaymentMethod());
        orderOnline.setOrderTime(Instant.now()); // Ensure order time is set
        orderOnline.setStatus(OrderStatus.PENDING); // Ensure status is set

        // Ensure order details are linked to the order entity
        if (orderOnlineDTO.getOrderDetails() != null) {
            orderOnlineDTO.getOrderDetails().forEach(detailDTO -> {
                OrderOnlineDetails detail = orderOnlineDetailsMapper.toEntity(detailDTO);
                orderOnline.addOrderDetail(detail); // Use the helper method to link both sides
            });
        }

        // Recalculate and set total amount based on entity details (safer after details are linked)
        BigDecimal totalAmount = orderOnline.getOrderDetails().stream()
                .map(detail -> detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        orderOnline.setTotalAmount(totalAmount);

        // Save the order and flush changes immediately
        OrderOnline savedOrder = orderOnlineRepository.saveAndFlush(orderOnline);

        // Log the savedOrder entity state after saving and flushing
        System.out.println("Saved OrderOnline entity after saving and flushing: " + savedOrder);

        return orderOnlineMapper.toDTO(savedOrder);
    }

    @Override
    public List<OrderOnlineDTO> getAllOrders() {
        return orderOnlineRepository.findAll().stream()
                .map(orderOnlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderOnlineDTO> getOrderById(Long id) {
        return orderOnlineRepository.findById(id)
                .map(orderOnlineMapper::toDTO);
    }

    @Override
    public List<OrderOnlineDTO> getOrdersByUserId(String userId) {
        return orderOnlineRepository.findByUserId(userId).stream()
                .map(orderOnlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderOnlineDTO> getOrdersByStatus(String status) {
        return orderOnlineRepository.findByStatus(OrderStatus.valueOf(status)).stream()
                .map(orderOnlineMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderOnlineDTO updateOrderStatus(Long orderId, String newStatus, Integer employeeId) {
        System.out.println("Updating order status for order ID: " + orderId + " to status: " + newStatus);
        System.out.println("Received employee ID: " + employeeId); // Log employeeId here

        OrderOnline order = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> {
                    System.err.println("Order not found for ID: " + orderId); // Log if order not found
                    return new RuntimeException("Order not found");
                });

        order.setStatus(OrderStatus.valueOf(newStatus));
        if (OrderStatus.valueOf(newStatus) == OrderStatus.DELIVERING) {
            order.setDeliveryTime(Instant.now());
        }

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> {
                        System.err.println("Employee not found for ID: " + employeeId); // Log if employee not found
                        return new RuntimeException("Employee not found");
                    });
            order.setEmployee(employee);
            System.out.println("Set employee for order. Employee ID to be saved: " + employee.getId() + ", Name: " + employee.getName()); // Log employee info
        } else {
            System.out.println("Employee ID is null, not setting employee for order.");
            order.setEmployee(null); // Explicitly set to null if employeeId is null, to ensure consistency
        }

        OrderOnline updatedOrder = orderOnlineRepository.save(order);
        System.out.println("Order saved. Employee ID in saved order: " + (updatedOrder.getEmployee() != null ? updatedOrder.getEmployee().getId() : "null")); // Log saved employee ID
        return orderOnlineMapper.toDTO(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderOnline order = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Can only cancel pending orders");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderOnlineRepository.save(order);
    }

    @Override
    public List<OrderOnlineDetailsDTO> getOrderDetailsByOrderId(Long orderId) {
        return orderOnlineDetailsRepository.findByOrderOnlineId(orderId)
                .stream()
                .map(orderOnlineDetailsMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderOnlineDTO updateOrder(Long orderId, OrderOnlineDTO updatedOrderDTO) {
        OrderOnline existingOrder = orderOnlineRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Use MapStruct mapper to update fields from DTO to entity
        orderOnlineMapper.updateEntityFromDTO(updatedOrderDTO, existingOrder);

        // If status is updated to DELIVERING, set delivery time
         if (updatedOrderDTO.getStatus() == OrderStatus.DELIVERING && existingOrder.getDeliveryTime() == null) {
             existingOrder.setDeliveryTime(Instant.now());
         }

        OrderOnline updatedOrder = orderOnlineRepository.save(existingOrder);
        // Optionally load details if needed in the response DTO
        OrderOnlineDTO resultDTO = orderOnlineMapper.toDTO(updatedOrder);
        resultDTO.setOrderDetails(getOrderDetailsByOrderId(updatedOrder.getId()));

        return resultDTO;
    }

    @Override
    public OrderOnlineDTO updatePaymentProof(Long id, String paymentImageUrl) {
        OrderOnline order = orderOnlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setPaymentImage(paymentImageUrl);
        OrderOnline savedOrder = orderOnlineRepository.save(order);
        return orderOnlineMapper.toDTO(savedOrder);
    }
} 