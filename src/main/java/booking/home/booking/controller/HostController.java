package booking.home.booking.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.Order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
import booking.home.booking.repo.AccountRepo;
import booking.home.booking.repo.CustomerRepo;
import booking.home.booking.repo.HostRepo;
import booking.home.booking.repo.OrderDealRepo;
import booking.home.booking.repo.RoomRepo;
import booking.home.booking.repo.UserRepo;
import booking.home.booking.until.Validation;
import booking.home.booking.vo.common.DataTable;
import booking.home.booking.vo.host.DataPost;
import booking.home.booking.vo.order.DataPutBrowseOrder;
import booking.home.booking.vo.order.GetHostOrder;
import booking.home.booking.vo.order.GetHostOrderSuccess;

@RestController
@RequestMapping("host")
public class HostController {

	@Autowired
	HostRepo hostRepo;

	@Autowired
	UserRepo userRepo;

	@Autowired
	CustomerRepo customerRepo;
	
	@Autowired
	RoomRepo roomRepo;

	@Autowired
	OrderDealRepo orderDealRepo;

	
	
	@PostMapping("/")
	public ResponseEntity<DataTable> createAccount(@RequestBody Host host) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Optional<Host> hostCheck;
		Optional<User> user;

		if (host.getUserID() == null) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin userID");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (host.getIdCard() == null || host.getIdCard().isBlank() || host.getIdCard().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin ch???ng minh nh??n d??n");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (host.getFullname() == null || host.getFullname().isBlank() || host.getFullname().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin h??? t??n ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (host.getAddress().isBlank() || host.getAddress().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin ?????a ch???");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (host.getEmail().isBlank() || host.getEmail().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin email");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!Validation.isValidEmailAddress(host.getEmail())) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin email ????ng ?????nh d???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (host.getPhone().isBlank() || host.getPhone().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin s??? ??i???n tho???i");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		user = userRepo.findById(host.getUserID());

		if (user.isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Ch??a c?? th??ng tin userID");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!user.get().getRole().equals("host")) {
			reponse.setMessage("404");
			result.put("error", "UserID kh??ng c?? quy???n ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		hostCheck = hostRepo.findHostByuserID(host.getUserID());

		if (!hostCheck.isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "userID ???? t???n t???i v???i m???t ch???");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		host.setStatus(false);
		host.setTotalRevenue(BigDecimal.ZERO);

		hostRepo.save(host);

		reponse.setMessage("200");
		reponse.setData(host);
		return ResponseEntity.ok(reponse);

	}

	@GetMapping("/room/orderBrowse")
	public ResponseEntity<DataTable> getRoomOrderBrowse(@RequestHeader("Authorization") String token) {

		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Host hostCheck;
		User accountCheck;
		List<OrderDeal> orderList;

		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nh???p token v??o h??? th???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

		if (accountCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Token kh??ng ch??nh x??c");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!accountCheck.getRole().equals("host")) {
			reponse.setMessage("401");
			result.put("error", "Kh??ng ph???i quy???n ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!accountCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		hostCheck = hostRepo.findHostByuserID(accountCheck.getId()).orElse(null);

		if (hostCheck == null) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n hi???n t???i ch??a c?? ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!hostCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		orderList = orderDealRepo.findOrderDealByStatus("write");
		ArrayList<GetHostOrder> getOrderList = new ArrayList<>();
		if (orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				if (orderList.get(i).getHost().getId() == hostCheck.getId()) {
					GetHostOrder getOrder = new GetHostOrder();
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

	@PostMapping("/room/browse")
	public ResponseEntity<DataTable> browseOrder(@RequestHeader("Authorization") String token,
			@RequestBody DataPutBrowseOrder order) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Host hostCheck;
		User accountCheck;
		Room roomCheck;
		Customer customerUpdated;
		List<OrderDeal> orderList;
		ArrayList<OrderDeal> getOrderList = new ArrayList<OrderDeal>();

		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nh???p token v??o h??? th???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (order.getIdCustomer() == null) {
			reponse.setMessage("404");
			result.put("error", "Y??u c???u b???n nh???p th??ng tin kh??ch h??ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (order.getIdRoom() == null) {
			reponse.setMessage("404");
			result.put("error", "Y??u c???u b???n nh???p th??ng tin ph??ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (order.getTimeOrder() == null || order.getTimeOrder().trim() == "") {
			reponse.setMessage("404");
			result.put("error", "Y??u c???u b???n nh???p ?????t order");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

		if (accountCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Token kh??ng ch??nh x??c");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!accountCheck.getRole().equals("host")) {
			reponse.setMessage("401");
			result.put("error", "Kh??ng ph???i quy???n ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!accountCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		hostCheck = hostRepo.findHostByuserID(accountCheck.getId()).orElse(null);

		if (hostCheck == null) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n hi???n t???i ch??a c?? ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!hostCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		roomCheck = roomRepo.findById(order.getIdRoom()).orElse(null);
		
		if(roomCheck == null) {
			reponse.setMessage("401");
			result.put("error", "ph??ng b???n order kh??ng c?? trong h??? th???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(!roomCheck.getStatus().equals("runner")) {
			reponse.setMessage("401");
			result.put("error", "ph??ng b???n kh??ng th??? order v?? n?? kh??ng ph???i runner");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		orderList = orderDealRepo.findOrderDealByHost(hostCheck);

		if (orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				if (orderList.get(i).getStatus().equals("write")
						&& orderList.get(i).getTimeRoom().equals(order.getTimeOrder())
						&& orderList.get(i).getRoom().getId() == order.getIdRoom()) {
					getOrderList.add(orderList.get(i));
				}
			}
		}

		if (getOrderList.isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "L???i do h??? th???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		boolean custCheck = false;
		int positionPayment=0;
		long idCustomer = 0;
		for (int i = 0; i < getOrderList.size(); i++) {
			if (getOrderList.get(i).getCustomer().getId() == order.getIdCustomer()) {
				OrderDeal orderSet = getOrderList.get(i);
				orderSet.setStatus("payment");
				getOrderList.set(i, orderSet);
				custCheck = true;
				positionPayment=i;
				idCustomer = getOrderList.get(i).getCustomer().getId();
			} else {
				OrderDeal orderSet = getOrderList.get(i);
				orderSet.setStatus("cannel");
				getOrderList.set(i, orderSet);
			}

		}

		if (!custCheck) {
			reponse.setMessage("401");
			result.put("error", "Kh??ng t???n t???i kh??ch h??ng b???n ch???n");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		BigDecimal payMentSuccess = orderList.get(positionPayment).getTotalPay();
		BigDecimal totalRevenue = hostCheck.getTotalRevenue().add(payMentSuccess);
		hostCheck.setTotalRevenue(totalRevenue);
		
		if(idCustomer>0) {
			customerUpdated = customerRepo.findById(idCustomer).orElse(null);
			if(customerUpdated!=null) {
				BigDecimal totalBillCust  = customerUpdated.getTotalBillSuccess().add(payMentSuccess);
				int totalOrderCust  = customerUpdated.getTotalOrderSuccess() + 1;
				
				customerUpdated.setTotalBillSuccess(totalBillCust);
				customerUpdated.setTotalOrderSuccess(totalOrderCust);
				customerRepo.save(customerUpdated);
			}
			
		}
		int shipRoomCheck = roomCheck.getNumberShip();
		int shipRoomCheckAdd = shipRoomCheck+ 1; 
		roomCheck.setNumberShip(shipRoomCheckAdd);
		roomCheck.setStatus("order");
		roomRepo.save(roomCheck);
		hostRepo.save(hostCheck);
		
		
		
		orderDealRepo.saveAll(getOrderList);
		

		reponse.setMessage("200");
		reponse.setData(getOrderList);
		return ResponseEntity.ok(reponse);

	}

	@GetMapping("/order/success")
	public ResponseEntity<DataTable> getOrderSuccess(@RequestHeader("Authorization") String token){
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Host hostCheck;
		User accountCheck;
		List<OrderDeal> orderList;
		ArrayList<GetHostOrderSuccess> hostOrderSuccessList = new ArrayList<GetHostOrderSuccess>();
		
		
		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nh???p token v??o h??? th???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

		if (accountCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Token kh??ng ch??nh x??c");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!accountCheck.getRole().equals("host")) {
			reponse.setMessage("401");
			result.put("error", "Kh??ng ph???i quy???n ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!accountCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		hostCheck = hostRepo.findHostByuserID(accountCheck.getId()).orElse(null);

		if (hostCheck == null) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n hi???n t???i ch??a c?? ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!hostCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		orderList = orderDealRepo.findOrderDealByHost(hostCheck);
		
		if (orderList.size() > 0) {
			for (int i = 0; i < orderList.size(); i++) {
				if (orderList.get(i).getStatus().equals("payment")){
					GetHostOrderSuccess hostOrderSuccess = new GetHostOrderSuccess();
					hostOrderSuccess.setCustomerName(orderList.get(i).getCustomer().getFullname());
					hostOrderSuccess.setEmail(orderList.get(i).getCustomer().getEmail());
					hostOrderSuccess.setLegalNo(orderList.get(i).getCustomer().getIdCard());
					hostOrderSuccess.setHotelName(orderList.get(i).getRoom().getName());
					hostOrderSuccess.setPhone(orderList.get(i).getCustomer().getPhone());
					hostOrderSuccess.setPrice(orderList.get(i).getRoom().getPrice());
					hostOrderSuccess.setTimeOrder(orderList.get(i).getTimeRoom());
					hostOrderSuccess.setFullName(orderList.get(i).getCustomer().getFullname());
					hostOrderSuccess.setAddress(orderList.get(i).getCustomer().getAddress());
					hostOrderSuccessList.add(hostOrderSuccess);
				}
			}
		}
		
		if(hostOrderSuccessList.size()==0) {
			reponse.setMessage("200");
			return ResponseEntity.ok(reponse);
		}
		
		
		reponse.setMessage("200");
		reponse.setData(hostOrderSuccessList);
		return ResponseEntity.ok(reponse);
		
		
		
	}
	
	@PostMapping("/put")
	public ResponseEntity<DataTable> putHost(@RequestBody DataPost host,@RequestHeader("Authorization") String token) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Host hostCheck;
		User accountCheck;
		boolean updatePosition=false;
		
		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nh???p token v??o h??? th???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

		if (accountCheck == null) {
			reponse.setMessage("401");
			result.put("error", "Token kh??ng ch??nh x??c");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (!accountCheck.getRole().equals("host")) {
			reponse.setMessage("401");
			result.put("error", "Kh??ng ph???i quy???n ch??? nh??");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!accountCheck.getStatus()) {
			reponse.setMessage("401");
			result.put("error", "T??i kho???n c???a b???n ???? b??? kh??a");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		
		

		if (host.getUserID() == null) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin userID");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(host.getUserID()!=accountCheck.getId()) {
			reponse.setMessage("401");
			result.put("error", "Nh???p th??ng tin userID kh??ng ????ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		
		hostCheck = hostRepo.findHostByuserID(accountCheck.getId()).orElse(null);
		

		if (host.getIdCard() == null || host.getIdCard().isBlank() || host.getIdCard().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin cmnd");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			hostCheck.setIdCard(host.getIdCard());
		}

		if (host.getFullname() == null || host.getFullname().isBlank() || host.getFullname().isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Nh???p th??ng tin H??? v?? T??n");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			hostCheck.setFullname(host.getFullname());
		}

		if (host.getAddress().isBlank() || host.getAddress().isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin ?????a ch???");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			hostCheck.setAddress(host.getAddress());
		}

		if (host.getEmail().isBlank() || host.getEmail().isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Nh???p th??ng tin email");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else if (!Validation.isValidEmailAddress(host.getEmail())) {
			reponse.setMessage("404");
			result.put("error", "Nh???p th??ng tin email ????ng ?????nh d???ng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			hostCheck.setEmail(host.getEmail());
		}

		if (host.getPhone().isBlank() || host.getPhone().isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Nh???p th??ng tin s??? ??i???n tho???i");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}else {
			hostCheck.setPhone(host.getPhone());
		}

			hostRepo.save(hostCheck);
	
		
		
		

		reponse.setMessage("200");
		reponse.setData(host);
		return ResponseEntity.ok(reponse);

	}
}
