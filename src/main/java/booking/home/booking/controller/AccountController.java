package booking.home.booking.controller;

import booking.home.booking.enity.Customer;
import booking.home.booking.enity.Host;
import booking.home.booking.enity.Room;
import booking.home.booking.enity.User;
import booking.home.booking.repo.CustomerRepo;
import booking.home.booking.repo.HostRepo;
import booking.home.booking.repo.RoomRepo;
import booking.home.booking.repo.UserRepo;
import booking.home.booking.until.Validation;
import booking.home.booking.vo.account.Login;
import booking.home.booking.vo.account.Register;
import booking.home.booking.vo.account.GetHostBrowse;
import booking.home.booking.vo.common.DataTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	UserRepo userRepo;

	@Autowired
	HostRepo hostRepo;

	@Autowired
	RoomRepo roomRepo;
	
	@Autowired
	CustomerRepo cusRepo;

	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
	
	
	@PostMapping("")
	public ResponseEntity<DataTable> registerAccount(@RequestBody Register user) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		
		User userCheck;
		Host host = new Host();
		Customer customer = new Customer();
		
		User userSave = new User();
		
		if(user.getFullName()==null||user.getFullName().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập họ tên đầy đủ");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(user.getAddress()==null||user.getAddress().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập địa chỉ");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(user.getEmail()==null||user.getEmail().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập email");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!Validation.isValidEmailAddress(user.getEmail())) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin email đúng định dạng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(user.getLegalNo()==null||user.getLegalNo().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập email");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(user.getPhone()==null||user.getPhone().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập số điện thoại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(user.getUsername()==null||user.getUsername().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập số điện thoại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(user.getPassword()==null||user.getPassword().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập số điện thoại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if(!isValidPassword(user.getPassword())) {
			reponse.setMessage("404");
			result.put("error", "Yêu cầu bạn nhập password đúng chuẩn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(user.getRole()==null||user.getRole().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập quyền");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(user.getRole()==null||user.getRole().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập quyền");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(!user.getRole().matches("^(client|host)$")) {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập quyền đúng định dạng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		if(user.getAddress()==null||user.getAddress().trim()=="") {
			reponse.setMessage("404");
			result.put("error", "Vui lòng nhập số địa chỉ");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		
		userCheck = userRepo.findUserByUsername(user.getUsername()).orElse(null);
		
		if(userCheck!=null) {
			reponse.setMessage("401");
			result.put("error", "Username đã tồn tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		UUID uuid = UUID.randomUUID();
		String ramdomString = uuid.toString();
		
		
		userSave.setUsername(user.getUsername());
		userSave.setStatus(false);
		userSave.setToken(ramdomString);
		userSave.setPassword(user.getPassword());
		userSave.setRole(user.getRole());
		userRepo.save(userSave);
		User tokenCheck =userRepo.findUserRoleByToken(ramdomString).orElse(null);
		
		if(tokenCheck==null) {
			reponse.setMessage("401");
			result.put("error", "Hệ thống bị lỗi");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
	
		if(user.getRole().equals("host")) {
			
			host.setUserID(tokenCheck.getId());
			host.setIdCard(user.getLegalNo());
			host.setEmail(user.getEmail());
			host.setAddress(user.getAddress());
			host.setPhone(user.getPhone());
			host.setStatus(false);
			host.setTotalRevenue(BigDecimal.ZERO);
			host.setFullname(user.getFullName());
			
			hostRepo.save(host);
		}
		else if(user.getRole().equals("client")) {
			customer.setUserID(tokenCheck.getId());
			customer.setEmail(user.getEmail());
			customer.setFullname(user.getFullName());
			customer.setTotalBillSuccess(BigDecimal.ZERO);
			customer.setIdCard(user.getLegalNo());
			customer.setPhone(user.getPhone());
			customer.setTotalOrderSuccess(0);
			customer.setAddress(user.getAddress());
			customer.setStatus(false);
			cusRepo.save(customer);
		
		}else {
			reponse.setMessage("401");
			result.put("error", "Hệ thống bị lỗi");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
	
		reponse.setMessage("200");
		reponse.setData(user);
		return ResponseEntity.ok(reponse);
		
		
	}

	@PostMapping("createAccount/")
	public ResponseEntity<DataTable> createAccount(@RequestBody User user) {

		DataTable reponse = new DataTable();
		Map result = new HashMap();
		
		if (!isValidPassword(user.getPassword())) {
			reponse.setMessage("404");
			result.put("error", "Yêu cầu bạn nhập mật khẩu theo chuẩn");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		

		UUID uuid = UUID.randomUUID();
		String uuidAsString = uuid.toString();

		user.setToken(uuidAsString);

		userRepo.save(user);

		reponse.setMessage("200");
		reponse.setData(user);

		return ResponseEntity.ok(reponse);

	}

	@PostMapping("login/")
	public ResponseEntity<DataTable> loginAccount(@RequestBody Login user) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Optional<User> userCheck;

		if (user.getUsername().isEmpty() || user.getUsername().isBlank()) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin username");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (user.getPassword().isEmpty() || user.getPassword().isBlank()) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin password");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		userCheck = userRepo.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());

		if (userCheck.isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Bạn vui lòng kiểm tra thông tin đăng nhập");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!userCheck.get().getStatus()) {
			reponse.setMessage("404");
			result.put("error", "Tài khoản của bạn bị khóa");
			result.put("token", userCheck.get().getToken());
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		result.put("username", userCheck.get().getUsername());
		result.put("role", userCheck.get().getRole());
		result.put("token", userCheck.get().getToken());

		reponse.setMessage("200");
		reponse.setData(result);

		return ResponseEntity.ok(reponse);

	}

	@GetMapping("/inforAccount/")
	public ResponseEntity<DataTable> getInforAccount(@RequestHeader("Authorization") String token) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Optional<User> userCheck;

		if (token == null || token == "") {
			reponse.setMessage("404");
			result.put("error", "Nhập token vào hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		userCheck = userRepo.findUserRoleByToken(token);

		if (userCheck.isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Token của bạn không chính xác");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		if (!userCheck.get().getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Account của bạn đã bị khóa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		switch (userCheck.get().getRole()) {

		case "host":
			reponse.setMessage("200");
			result.put("infor", hostRepo.findHostByuserID(userCheck.get().getId()));
			result.put("role", "host");
			result.put("username", userCheck.get().getUsername());
			reponse.setData(result);
			break;
		case "client":
			reponse.setMessage("200");
			result.put("infor", cusRepo.findCustomerByuserID(userCheck.get().getId()));
			result.put("role", "client");
			result.put("username", userCheck.get().getUsername());
			reponse.setData(result);
			break;
		case "admin":
			reponse.setMessage("200");
			result.put("role", "admin");
			result.put("username", userCheck.get().getUsername());
			reponse.setData(result);
			break;
		default:
			reponse.setMessage("200");
			result.put("username", userCheck.get().getUsername());
			result.put("token", userCheck.get().getToken());
			reponse.setData(result);
		}

		return ResponseEntity.ok(reponse);

	}
	
	
	
	
	// updated account
	@GetMapping("status/{id}/{bool}")
	public ResponseEntity<DataTable> updatedStatusAccount(@RequestHeader("Authorization") String token,
			@PathVariable("id") Long id, @PathVariable("bool") Boolean bool) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Optional<User> userRole;
		String role;
		Optional<User> userUpdated;
		Optional<Host> userHost;
		Optional<Customer> userCus;

		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nhập token vào hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (id == null) {
			reponse.setMessage("404");
			result.put("error", "Nhập id vào hệ thống");
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
		if (!role.equals("admin")) {
			reponse.setMessage("401");
			result.put("error", "Bạn không có quyền admin truy cập");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		userUpdated = userRepo.findById(id);
		if (userUpdated.isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nhập user không tồn tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		userRepo.setHostStatusById(bool, id);

		if (userUpdated.get().getRole().equals("host")) {
			userHost = hostRepo.findHostByuserID(id);

			if (userHost.isEmpty()) {
				reponse.setMessage("404");
				result.put("error", "Nhập user host không tồn tại");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			hostRepo.setHostStatusById(bool, id);
		}else if(userUpdated.get().getRole().equals("client")) {
			userCus = cusRepo.findCustomerByuserID(id);

			if (userCus.isEmpty()) {
				reponse.setMessage("404");
				result.put("error", "Nhập user khách hàng không tồn tại");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			cusRepo.setCustomerStatusById(bool, id);
		}
		
		
		

		reponse.setMessage("200");
		return ResponseEntity.ok(reponse);

	}

	

	// get list host and customer
	@GetMapping("browse/{bool}")
	public ResponseEntity<DataTable> getHostBrowse(@RequestHeader("Authorization") String token, @PathVariable("bool") Boolean bool){
		
			DataTable reponse = new DataTable();
			Map result = new HashMap();
			Optional<User> userCheck;
			ArrayList<GetHostBrowse> ListHostBrowse = new ArrayList<GetHostBrowse>();
			List<User> listUser;
			
			if (token == null || token == "") {
				reponse.setMessage("401");
				result.put("error", "Nhập token vào hệ thống");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			
			userCheck = userRepo.findUserRoleByToken(token);

			if (userCheck.isEmpty()) {
				reponse.setMessage("401");
				result.put("error", "Token của bạn không chính xác");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			if (!userCheck.get().getStatus()) {
				reponse.setMessage("401");
				result.put("error", "Account của bạn đã bị khóa");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			
			if(!userCheck.get().getRole().equals("admin")) {
				reponse.setMessage("401");
				result.put("error", "Account của bạn không có quyền admin");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			
		
			
			listUser = userRepo.findUserByStatus(bool);
			
			if(listUser.size()==0) {
				reponse.setMessage("200");
				return ResponseEntity.ok(reponse);
			}
			for (int i =0;i<listUser.size();i++) {
				GetHostBrowse getbrowse = new GetHostBrowse(); 
				if(listUser.get(i).getRole().equals("client")) {
					Customer customerBrowse = cusRepo.findCustomerByuserID(listUser.get(i).getId()).orElse(null);
					
					if(customerBrowse!=null) {
						getbrowse.setIdUser(listUser.get(i).getId());
						getbrowse.setAddress(customerBrowse.getAddress());
						getbrowse.setEmail(customerBrowse.getEmail());
						getbrowse.setLegalNo(customerBrowse.getIdCard());
						getbrowse.setFullName(customerBrowse.getFullname());
						getbrowse.setRole("client");
						getbrowse.setPhone(customerBrowse.getPhone());
						getbrowse.setUsername(listUser.get(i).getUsername());
						ListHostBrowse.add(getbrowse);
					}
				}else if(listUser.get(i).getRole().equals("host")) {
					Host hostBrowse = hostRepo.findHostByuserID(listUser.get(i).getId()).orElse(null);
					if(hostBrowse!=null) {
						getbrowse.setIdUser(listUser.get(i).getId());
						getbrowse.setAddress(hostBrowse.getAddress());
						getbrowse.setEmail(hostBrowse.getEmail());
						getbrowse.setLegalNo(hostBrowse.getIdCard());
						getbrowse.setFullName(hostBrowse.getFullname());
						getbrowse.setRole("host");
						getbrowse.setPhone(hostBrowse.getPhone());
						getbrowse.setUsername(listUser.get(i).getUsername());
						ListHostBrowse.add(getbrowse);
					}
					
				}else {
					// not working
				}
			}
			
			
			
			reponse.setMessage("200");
			reponse.setData(ListHostBrowse);
			return ResponseEntity.ok(reponse);
			
			
			
			
			
	}

	// updated order 
	@GetMapping("/updatedStatusRoom/{room_id}/{status}")
	public ResponseEntity<DataTable> createAccount(@RequestHeader("Authorization") String token,
			@PathVariable("room_id") Long room_id, @PathVariable("status") String status) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Optional<User> userCheck;
		Optional<Host> hostCheck;
		Optional<Room> roomCheck;
		// validation token
		if (token == null || token == "") {
			reponse.setMessage("401");
			result.put("error", "Nhập token vào hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		// validation status
		if (!status.matches("^(pending|runner|order)$")) {
			reponse.setMessage("404");
			result.put("error", "Nhập không đúng status trong hệ thộng hiện tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		// get user from db and check data
		userCheck = userRepo.findUserRoleByToken(token);
		if (userCheck.isEmpty() || !userCheck.get().getStatus()) {
			reponse.setMessage("401");
			result.put("error", "Nhập token không tìm thấy user trong hệ thống");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		

		// get room from db and check data
		roomCheck = roomRepo.findById(room_id);

		if (roomCheck.isEmpty()) {
			reponse.setMessage("401");
			result.put("error", "Nhập phòng này không tồn tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		

		// switch host admin logic updated
		switch (userCheck.get().getRole()) {
		case "host":
			// get host from db and check data
			hostCheck = hostRepo.findHostByuserID(userCheck.get().getId());
			if (hostCheck.isEmpty() || !hostCheck.get().getStatus()) {
				reponse.setMessage("401");
				result.put("error", "Hiện tại account này không tồn có thông tin chủ nhà");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			
			// host show client
			if (roomCheck.get().getStatus().equals("pending") && status.equals("runner")) {
				//
				int numberShip = roomCheck.get().getNumberShip();
				roomCheck.get().setNumberShip(numberShip+1);
				roomCheck.get().setStatus("runner");
				roomRepo.save(roomCheck.get());
				
				//roomRepo.setRoomStatusById("runner", roomCheck.get().getId());
				reponse.setMessage("200");
				result.put("status", "runner");
				reponse.setData(result);
			} else if (roomCheck.get().getStatus().equals("runner") && status.equals("order")) {
				//
				roomRepo.setRoomStatusById("order", roomCheck.get().getId());
				reponse.setMessage("200");
				result.put("status", "order");
				reponse.setData(result);
			}else if (roomCheck.get().getStatus().equals("order") && status.equals("runner")) {
				//
				roomRepo.setRoomStatusById("runner", roomCheck.get().getId());
				reponse.setMessage("200");
				result.put("status", "runner");
				reponse.setData(result);
			}
			else {
				reponse.setMessage("401");
				result.put("error", "Bạn đưa logic trạng thái cho chủ nhà không hợp lý");
				reponse.setData(result);

			}
			break;
		case "admin":
			if (roomCheck.get().getStatus().equals("write") && status.equals("pending")) {
				//
				//roomRepo.setRoomStatusById("pending", roomCheck.get().getId());
				
				roomCheck.get().setStatus("pending");
				roomRepo.save(roomCheck.get());
				reponse.setMessage("200");
				result.put("status", "pending");
				reponse.setData(result);
			} else {
				reponse.setMessage("401");
				result.put("error", "Bạn đưa logic trạng thái cho chủ nhà không hợp lý");
				reponse.setData(result);

			}

			break;
		default:
			reponse.setMessage("401");
			result.put("error", "Phòng bị lỗi hiện tại không có trạng thái");
			reponse.setData(result);
			break;
		}

		return ResponseEntity.ok(reponse);

	}
	
	public static boolean isValidPassword(String password) {
		boolean isValid = true;
		if (password.length() > 15 || password.length() < 8) {

			isValid = false;
		}
		String upperCaseChars = "(.*[A-Z].*)";
		if (!password.matches(upperCaseChars)) {

			isValid = false;
		}
		String lowerCaseChars = "(.*[a-z].*)";
		if (!password.matches(lowerCaseChars)) {
			isValid = false;
		}
		String numbers = "(.*[0-9].*)";
		if (!password.matches(numbers)) {
			isValid = false;
		}
		String specialChars = "(.*[@,#,$,%].*$)";
		if (!password.matches(specialChars)) {
			isValid = false;
		}
		return isValid;
	}

}
