package booking.home.booking.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import booking.home.booking.enity.Customer;
import booking.home.booking.enity.Host;
import booking.home.booking.enity.OrderDeal;
import booking.home.booking.enity.User;
import booking.home.booking.repo.CustomerRepo;
import booking.home.booking.repo.OrderDealRepo;
import booking.home.booking.repo.UserRepo;
import booking.home.booking.until.Validation;
import booking.home.booking.vo.common.DataTable;
import booking.home.booking.vo.customer.DataPost;
import booking.home.booking.vo.order.GetCustomerOrder;
import booking.home.booking.vo.order.GetHostOrder;

@RestController
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	OrderDealRepo orderDealRepo;
	
	@Autowired
	UserRepo userRepo;
	
		
	
	@PostMapping("/put")
	public ResponseEntity<DataTable> updatedCustomer(@RequestHeader("Authorization") String token,@RequestBody DataPost customer){
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		User userCheck;
		Customer customerCheck;
		
		if(token==null||token.trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin token");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		userCheck = userRepo.findUserRoleByToken(token).orElse(null);
		
		if(userCheck==null) {
			reponse.setMessage("401");
			result.put("error", "Nhập thông tin không có trong hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(!userCheck.getRole().equals("client")) {
			reponse.setMessage("401");
			result.put("error", "Nhập thông tin quyền không đúng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		if (customer.getUserID() == null) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin userID");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(customer.getUserID()!=userCheck.getId()) {
			reponse.setMessage("401");
			result.put("error", "Nhập thông tin userID không đúng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		
		customerCheck = customerRepo.findCustomerByuserID(userCheck.getId()).orElse(null);
		if(customerCheck==null) {
			reponse.setMessage("404");
			result.put("error", "userID đã tồn tại với một khách hàng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(customer.getFullname()==null || customer.getFullname()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin họ tên bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			customerCheck.setFullname(customer.getFullname());
		}
		if(customer.getEmail()==null || customer.getEmail()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin email của bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else if(!Validation.isValidEmailAddress(customer.getEmail())) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin email đúng định dạng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		else {
			customerCheck.setEmail(customer.getEmail());
			
		}
		
		if(customer.getAddress()==null || customer.getAddress()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin địa chỉ của bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			customerCheck.setAddress(customer.getAddress());
		}
		
		if(customer.getIdCard()==null || customer.getIdCard()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin chứng minh nhân dân");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			customerCheck.setIdCard(customer.getIdCard());
		}
		
		if(customer.getPhone()==null || customer.getPhone()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin số điện thoại của bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			customerCheck.setPhone(customer.getPhone());
		}
		
		if(customer.getMap()==null || customer.getMap()=="") {
			
		}else {
			customerCheck.setPhone(customer.getMap());
		}
		

	
		
		customerRepo.save(customerCheck);
		
		reponse.setMessage("200");
		reponse.setData(customer);
		return ResponseEntity.ok(reponse);
		
		
	}
	
//	@PostMapping("list/")
//	public ResponseEntity<DataTable> getAllCustomer(@RequestHeader("Authorization") String token){
//		DataTable reponse = new DataTable();
//		Map result = new HashMap();
//		Optional<User> userRole;
//		String role;
//		
//		if (token == null || token == "") {
//			reponse.setMessage("401");
//			result.put("error", "Nhập token vào hệ thống");
//			reponse.setData(result);
//			return ResponseEntity.ok(reponse);
//		}
//		
//		userRole = userRepo.findUserRoleByToken(token);
//
//		if (userRole.isEmpty()) {
//			reponse.setMessage("401");
//			result.put("error", "Nhập token không có trong hệ thống");
//			reponse.setData(result);
//			return ResponseEntity.ok(reponse);
//		}
//
//		role = userRole.get().getRole();
//		if (!role.equals("admin")) {
//			reponse.setMessage("401");
//			result.put("error", "Bạn không có quyền admin truy cập");
//			reponse.setData(result);
//			return ResponseEntity.ok(reponse);
//		}
//	}
	@PostMapping("/")
	public ResponseEntity<DataTable> createCustomer(@RequestHeader("Authorization") String token,@RequestBody DataPost customer){
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		User userCheck;
		Optional<Customer> customerCheck;
		
		if(token==null||token.trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin token");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		userCheck = userRepo.findUserRoleByToken(token).orElse(null);
		if(userCheck==null) {
			reponse.setMessage("401");
			result.put("error", "Nhập thông tin không có trong hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(!userCheck.getRole().equals("client")) {
			reponse.setMessage("401");
			result.put("error", "Nhập thông tin quyền không đúng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(customer.getFullname()==null || customer.getFullname()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin họ tên bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(customer.getEmail()==null || customer.getEmail()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin email của bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(!Validation.isValidEmailAddress(customer.getEmail())) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin email đúng định dạng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(customer.getAddress()==null || customer.getAddress()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin địa chỉ của bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(customer.getIdCard()==null || customer.getIdCard()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin chứng minh nhân dân");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(customer.getPhone()==null || customer.getPhone()=="") {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin số điện thoại của bạn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		customerCheck = customerRepo.findCustomerByuserID(userCheck.getId());
		if(!customerCheck.isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "userID đã tồn tại với một khách hàng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		Customer customerSave = new Customer();
		customerSave.setStatus(false);
		customerSave.setFullname(customer.getFullname());
		customerSave.setEmail(customer.getEmail());
		customerSave.setIdCard(customer.getIdCard());
		customerSave.setMap(customer.getMap());
		customerSave.setPhone(customer.getPhone());
		customerSave.setUserID(userCheck.getId());
		customerSave.setAddress(customer.getAddress());
		
		customerRepo.save(customerSave);
		
		reponse.setMessage("200");
		reponse.setData(customer);
		return ResponseEntity.ok(reponse);
		
		
	}

	@GetMapping("/room/orderBrowse")
	public ResponseEntity<DataTable> getRoomOrderBrowse(@RequestHeader("Authorization") String token) {
		
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Customer custCheck;
		User accountCheck;
		List<OrderDeal> orderList;

		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nhập token vào hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

		if (accountCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Token không chính xác");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!accountCheck.getRole().equals("client")) {
			reponse.setMessage("401");
			result.put("error", "Không phải quyền khách hàng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!accountCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản của bạn đã bị khóa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		custCheck = customerRepo.findCustomerByuserID(accountCheck.getId()).orElse(null);

		if (custCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản hiện tại chưa có ai đăng ký");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!custCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản của bạn đã bị khóa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		orderList = orderDealRepo.findOrderDealByStatus("write");
		ArrayList<GetCustomerOrder> getOrderList = new ArrayList<>();
		
		if (orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				if (orderList.get(i).getCustomer().getId() == custCheck.getId()) {
					GetCustomerOrder getOrder = new GetCustomerOrder();
					getOrder.setIdRoom(orderList.get(i).getRoom().getId());
					getOrder.setIdCustomer(orderList.get(i).getCustomer().getId());
					getOrder.setPhone(orderList.get(i).getCustomer().getPhone());
					getOrder.setEmail(orderList.get(i).getCustomer().getEmail());
					getOrder.setCustomerName(orderList.get(i).getCustomer().getFullname());
					getOrder.setHotelName(orderList.get(i).getRoom().getName());
					getOrder.setPrice(orderList.get(i).getRoom().getPrice());
					getOrder.setNote(orderList.get(i).getNote());
					getOrder.setTimeOrder(orderList.get(i).getTimeRoom());
					getOrderList.add(getOrder);
				}
			}
		}
		
		if (getOrderList.size() == 0) {
			reponse.setMessage("401");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		reponse.setMessage("200");
		reponse.setData(getOrderList);
		return ResponseEntity.ok(reponse);

	}
	
	
	@GetMapping("/room/orderSucess")
	public ResponseEntity<DataTable> getRoomOrderSuccess(@RequestHeader("Authorization") String token) {
		
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Customer custCheck;
		User accountCheck;
		List<OrderDeal> orderList;

		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nhập token vào hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

		if (accountCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Token không chính xác");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!accountCheck.getRole().equals("client")) {
			reponse.setMessage("401");
			result.put("error", "Không phải quyền khách hàng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!accountCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản của bạn đã bị khóa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		custCheck = customerRepo.findCustomerByuserID(accountCheck.getId()).orElse(null);

		if (custCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản hiện tại chưa có ai đăng ký");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!custCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản của bạn đã bị khóa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		orderList = orderDealRepo.findOrderDealByStatus("payment");
		ArrayList<GetCustomerOrder> getOrderList = new ArrayList<>();
		
		if (orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				if (orderList.get(i).getCustomer().getId() == custCheck.getId()) {
					GetCustomerOrder getOrder = new GetCustomerOrder();
					getOrder.setIdRoom(orderList.get(i).getRoom().getId());
					getOrder.setIdCustomer(orderList.get(i).getCustomer().getId());
					getOrder.setPhone(orderList.get(i).getCustomer().getPhone());
					getOrder.setEmail(orderList.get(i).getCustomer().getEmail());
					getOrder.setCustomerName(orderList.get(i).getCustomer().getFullname());
					getOrder.setHotelName(orderList.get(i).getRoom().getName());
					getOrder.setPrice(orderList.get(i).getRoom().getPrice());
					getOrder.setNote(orderList.get(i).getNote());
					getOrder.setTimeOrder(orderList.get(i).getTimeRoom());
					getOrderList.add(getOrder);
				}
			}
		}
		
		if (getOrderList.size() == 0) {
			reponse.setMessage("401");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		reponse.setMessage("200");
		reponse.setData(getOrderList);
		return ResponseEntity.ok(reponse);

	}

}



