package booking.home.booking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import booking.home.booking.enity.Customer;
import booking.home.booking.enity.Host;
import booking.home.booking.enity.OrderDeal;
import booking.home.booking.enity.Room;
import booking.home.booking.enity.User;
import booking.home.booking.repo.CustomerRepo;
import booking.home.booking.repo.HostRepo;
import booking.home.booking.repo.OrderDealRepo;
import booking.home.booking.repo.RoomRepo;
import booking.home.booking.repo.UserRepo;
import booking.home.booking.vo.common.DataTable;
import booking.home.booking.vo.order.DataPost;

@RestController
@RequestMapping("order")
public class OrderController {

	@Autowired
	OrderDealRepo orderDealRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoomRepo roomRepo;

	@Autowired
	CustomerRepo custRepo;

	@Autowired
	HostRepo hostRepo;
	
	
	

	@PostMapping("")
	public ResponseEntity<DataTable> createAccount(@RequestBody DataPost order,
			@RequestHeader("Authorization") String token) {

		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Optional<User> userRole;
		Optional<Customer> customerCheck;
		String role;
		Room roomCheck;

		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nhập token vào hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		userRole = userRepo.findUserRoleByToken(token);

		if (userRole.isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Nhập token không có trong hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		role = userRole.get().getRole();

		if (!role.equals("client")) {
			reponse.setMessage("401");
			result.put("error", "Bạn không được phép dùng quyền khách hàng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!userRole.get().getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Tài khoản của bạn đã bị khóa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		customerCheck = custRepo.findCustomerByuserID(userRole.get().getId());

		if (customerCheck.isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Khách hàng có tài khoản nhưng không có thông tin hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

//		if (order.getNote() == null || order.getNote().trim() == "") {
//			reponse.setMessage("404");
//			result.put("error", "Vui lòng nhập thông tin ghi chú về người đi cùng");
//			reponse.setData(result);
//			return ResponseEntity.ok(reponse);
//		}

		if (order.getRoom() == null || order.getRoom().trim() == "") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập thông tin phòng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		roomCheck = roomRepo.findById(Long.parseLong(order.getRoom())).orElse(null);

		if (roomCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Phòng nhà ở của bạn bị sai");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!roomCheck.getStatus().equals("runner")) {
			reponse.setMessage("401");
			result.put("error", "Lỗi do hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		OrderDeal orderSave = new OrderDeal();
		Room roomGet = roomRepo.findById(Long.parseLong(order.getRoom())).orElse(null);
		if (roomGet == null) {
			reponse.setMessage("401");
			result.put("error", "Lỗi do phòng không tồn tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (roomGet.getHost() == null) {
			reponse.setMessage("401");
			result.put("error", "Lỗi do phòng không tồn tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		String timeRoom = "time_" + roomCheck.getNumberShip();
//		List<OrderDeal> orderCheckIssetList =  orderDealRepo.findOrderDealBytimeRoom(timeRoom);
//		for(int i=0 ; i< orderCheckIssetList.size();i++) {
//			if(orderCheckIssetList.get(i).getCustomer().getId()==customerCheck.get().getId()&&orderCheckIssetList.get(i).getRoom().getId()==roomCheck.getId()) {
//				reponse.setMessage("401");
//				result.put("error", "Bạn đang đặt phòng rồi đang đợi phê duyệt");
//				reponse.setData(result);
//				return ResponseEntity.ok(reponse);
//			}
//		}
		
	
		

		orderSave.setHost(roomGet.getHost());
		orderSave.setRoom(roomGet);
		
		

		orderSave.setCustomer(customerCheck.get());
		orderSave.setTimeRoom(timeRoom);
		orderSave.setNote(order.getNote());
		orderSave.setTotalPay(roomCheck.getPrice());
		orderSave.setStatus("write");
		orderDealRepo.save(orderSave);

		reponse.setMessage("200");
		reponse.setData(order);
		return ResponseEntity.ok(reponse);

	}
}
