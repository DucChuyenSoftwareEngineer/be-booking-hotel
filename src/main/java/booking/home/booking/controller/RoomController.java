package booking.home.booking.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;
import java.util.Set;

import booking.home.booking.enity.Host;
import booking.home.booking.enity.Province;
import booking.home.booking.enity.Room;
import booking.home.booking.enity.RoomImg;
import booking.home.booking.enity.RoomType;
import booking.home.booking.enity.User;
import booking.home.booking.payload.UploadFileResponse;
import booking.home.booking.repo.HostRepo;
import booking.home.booking.repo.ProvinceRepo;
import booking.home.booking.repo.RoomImgRepo;
import booking.home.booking.repo.RoomRepo;
import booking.home.booking.repo.RoomTypeRepo;
import booking.home.booking.repo.UserRepo;
import booking.home.booking.service.FileStorageService;
import booking.home.booking.vo.common.DataTable;
import booking.home.booking.vo.room.DataGet;
import booking.home.booking.vo.room.DataGetIndex;
import booking.home.booking.vo.room.DataPost;
import booking.home.booking.vo.sco.RoomSco;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("room")
public class RoomController {

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private RoomRepo roomRepo;

	@Autowired
	private RoomImgRepo roomImgRepo;

	@Autowired
	private ProvinceRepo provinceRepo;
	
	@Autowired
	private HostRepo hostRepo;
	
	@Autowired
	private RoomTypeRepo roomTypeRepo;
	
	@Autowired
	UserRepo userRepo;

	private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

	@GetMapping("/search?city={}")
	public ResponseEntity<?> all() {
		DataTable reponse = new DataTable();
		Map result = new HashMap();

		return ResponseEntity.ok("Hello Manh");
	}
	
	@GetMapping
	public ResponseEntity<DataTable> getAll(RoomSco sco){
		logger.info(sco.toString());
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		List<Room> roomList = roomRepo.findAll();
		ArrayList<DataGetIndex> roomShowList = new ArrayList<>();
		if(roomList.size()==0) {
			reponse.setMessage("404");
			result.put("error", "Không có dữ liệu");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		for(int i=0;i<roomList.size();i++) {
			DataGetIndex dataGet = new DataGetIndex();
			if(roomList.get(i)!=null&&roomList.get(i).getStatus().equals("runner")) {
				
					dataGet.setId(roomList.get(i).getId());
					dataGet.setName(roomList.get(i).getName());
					dataGet.setRoomProfileImage("http://localhost:9000/images/"+roomList.get(i).getRoomProfileImage());
					dataGet.setStreetDetail(roomList.get(i).getStreetDetail());
					dataGet.setPrice(roomList.get(i).getPrice());
					roomShowList.add(dataGet);
				
			}
			
		}
		
		if(roomShowList.size()==0) {
			reponse.setMessage("404");
			result.put("error", "Không có dữ liệu");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		reponse.setMessage("200");
		
		reponse.setData(roomShowList);
		return ResponseEntity.ok(reponse);
		
		
		
	}
	
	@GetMapping("browse")
	public ResponseEntity<DataTable> roomBrowse(@RequestHeader("Authorization") String token) {
	
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		User accountCheck;
		ArrayList<DataGet> roomBrowse = new ArrayList<>();
		
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

		if (!accountCheck.getRole().equals("admin")) {
			reponse.setMessage("401");
			result.put("error", "Không phải quyền chủ nhà");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
	
		List<Room> roomCheck = roomRepo.findAll();
		
		for (Room room : roomCheck) {
			if(room.getStatus().equals("write")) {
				DataGet dataGet = new DataGet();
				dataGet.setId(room.getId());
				dataGet.setAreaRoom(room.getAreaRoom());
				dataGet.setBedTotal(room.getBedTotal());
				dataGet.setDescription(room.getDescription());
				dataGet.setGuidelineCheckin(room.getGuidelineCheckin());
				dataGet.setName(room.getName());
				dataGet.setMapLink(room.getMaps());
				dataGet.setNameProvince(room.getProvince().getName());
				dataGet.setMaxDateAmount(room.getMaxDateAmount());
				dataGet.setMaxPeopleAmount(room.getMaxPeopleAmount());
				dataGet.setNameType(room.getRoomType().getName());
				dataGet.setStreetDetail(room.getStreetDetail());
				dataGet.setRoomTotal(room.getRoomTotal());
				dataGet.setPrice(room.getPrice());
				dataGet.setStatus(room.getStatus());
				dataGet.setNameHost(room.getHost().getFullname());
				roomBrowse.add(dataGet);
			
			}
		}

		
		
		reponse.setMessage("200");
		
		reponse.setData(roomBrowse);
		return ResponseEntity.ok(reponse);
		
	}
	
	@GetMapping("browseHost")
	public ResponseEntity<DataTable> roomBrowseHost(@RequestHeader("Authorization") String token) {
	
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		User accountCheck;
		Host hostCheck;
		ArrayList<DataGet> roomBrowse = new ArrayList<>();
		
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

		if (!accountCheck.getRole().equals("host")) {
			reponse.setMessage("401");
			result.put("error", "Không phải quyền chủ nhà");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
	
		hostCheck = hostRepo.findHostByuserID(accountCheck.getId()).orElse(null);
		
		if (hostCheck == null) {
			reponse.setMessage("401");
			result.put("error", "chủ phòng không tồn tại");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		List<Room> roomCheck = roomRepo.findAll();
		
		for (Room room : roomCheck) {
			if(room.getHost().getId()==hostCheck.getId()) {
				if(room.getStatus().equals("order")||room.getStatus().equals("pending")) {
					DataGet dataGet = new DataGet();
					dataGet.setId(room.getId());
					dataGet.setAreaRoom(room.getAreaRoom());
					dataGet.setBedTotal(room.getBedTotal());
					dataGet.setDescription(room.getDescription());
					dataGet.setGuidelineCheckin(room.getGuidelineCheckin());
					dataGet.setName(room.getName());
					dataGet.setMapLink(room.getMaps());
					dataGet.setNameProvince(room.getProvince().getName());
					dataGet.setMaxDateAmount(room.getMaxDateAmount());
					dataGet.setMaxPeopleAmount(room.getMaxPeopleAmount());
					dataGet.setNameType(room.getRoomType().getName());
					dataGet.setStreetDetail(room.getStreetDetail());
					dataGet.setRoomTotal(room.getRoomTotal());
					dataGet.setPrice(room.getPrice());
					dataGet.setStatus(room.getStatus());
					dataGet.setNameHost(room.getHost().getFullname());
					roomBrowse.add(dataGet);
				
				}
			}
			
		}

		
		
		reponse.setMessage("200");
		
		reponse.setData(roomBrowse);
		return ResponseEntity.ok(reponse);
		
	}
	
	// cho client
	@GetMapping("/{room_id}/")
	public ResponseEntity<DataTable> roomByid(@PathVariable("room_id") Long room_id) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		String province;
		String hostFullName;
		DataGet dataGet = new DataGet();

		Room room = roomRepo.findById(room_id).orElse(null);

		if (room == null) { 
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin id không đúng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		
		if(!room.getStatus().equals("runner")) {
			reponse.setMessage("401");
			result.put("error", "Phòng này hiện tại đóng cửa");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}
		dataGet.setId(room.getId());
		dataGet.setName(room.getName());
		dataGet.setDescription(room.getDescription());
		dataGet.setGuidelineCheckin(room.getGuidelineCheckin());
		dataGet.setGuidelineUsingService(room.getGuidelineUsingService());
		dataGet.setBedTotal(room.getBedTotal());
		dataGet.setMaxDateAmount(room.getMaxDateAmount());
		dataGet.setMaxPeopleAmount(room.getMaxPeopleAmount());
		dataGet.setNameHost(room.getHost().getFullname());
		dataGet.setNameProvince(room.getProvince().getName());
		dataGet.setNameType(room.getRoomType().getName());
		dataGet.setPrice(room.getPrice());
		dataGet.setAreaRoom(room.getAreaRoom());
		dataGet.setStreetDetail(room.getStreetDetail());
		dataGet.setMapLink(room.getMaps());
		List<String> listImg= new ArrayList<>();
		List<RoomImg> roomImgList =  room.getRoomImgs();

		for(int i=0;i<roomImgList.size();i++) {
			listImg.add("http://localhost:9000/images/"+roomImgList.get(i).getUrl());
		}
		dataGet.setUrlImg(listImg);

		result.put("infor", dataGet);

			
		reponse.setMessage("200");
		reponse.setData(result);
		reponse.setData(result);
		return ResponseEntity.ok(reponse);
	}
	
	
	

	@PostMapping("")
	public ResponseEntity<DataTable> roomPost(@RequestBody DataPost room,@RequestHeader("Authorization") String token) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		Host hostCheck;
		Province provinceCheck;
		RoomType roomTypeCheck;
		User accountCheck;
	

		try {

			accountCheck = userRepo.findUserRoleByToken(token).orElse(null);

			if (accountCheck == null) {
				reponse.setMessage("401");
				result.put("error", "Token không chính xác");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}

			if (!accountCheck.getRole().equals("host")) {
				reponse.setMessage("401");
				result.put("error", "Không phải quyền chủ nhà");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			if (!accountCheck.getStatus()) {
				reponse.setMessage("401");
				result.put("error", "Tài khoản của bạn đã bị khóa");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}

			hostCheck = hostRepo.findHostByuserID(accountCheck.getId()).orElse(null);

			if (hostCheck == null) {
				reponse.setMessage("401");
				result.put("error", "Tài khoản hiện tại chưa có chủ nhà");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}

			if (!hostCheck.getStatus()) {
				reponse.setMessage("401");
				result.put("error", "Tài khoản của bạn đã bị khóa");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			
			if (room.getName() == null || room.getName().trim() == "") {
				reponse.setMessage("404");
				result.put("error", "Nhập thông tin tên phòng");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}

			if (room.getPrice() == null || room.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
				reponse.setMessage("404");
				result.put("error", "Nhập thông tin giá phòng");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}
			if(room.getAreaRoom()==null||room.getAreaRoom()<10) {
				reponse.setMessage("404");
				result.put("error", "Nhập thông tin diện tích ít nhất lớn 10 m2");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}

			if (room.getIdProvince() == null || room.getIdProvince().trim() == "") {
				reponse.setMessage("404");
				result.put("error", "Nhập thông tin Tỉnh Thành Phố");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			} else {
				provinceCheck = provinceRepo.findByProvinceid(room.getIdProvince()).orElse(null);
				if (provinceCheck == null) {
					reponse.setMessage("404");
					result.put("error", "Nhập thông tin Tỉnh Thành Phố không có trong hệ thống");
					reponse.setData(result);
					return ResponseEntity.ok(reponse);
				}
			}
			if(room.getIdType()==null || room.getIdType().trim()=="") {
				reponse.setMessage("404");
				result.put("error", "Nhập thông tin loại phòng");
				reponse.setData(result);
				return ResponseEntity.ok(reponse);
			}else {
				roomTypeCheck = roomTypeRepo.findById(Long.parseLong(room.getIdType())).orElse(null);
				if(roomTypeCheck == null) {
					reponse.setMessage("404");
					result.put("error", "Nhập thông tin loại phòng không có trong hệ thống");
					reponse.setData(result);
					return ResponseEntity.ok(reponse);
				}
			}

		} catch (NullPointerException ex) {

			reponse.setMessage("404");
			result.put("error", "Thông tin nhập bị lỗi");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		room.setStatus("write");
		
		
		// convert data vo to enity room
		Room roomSave = new Room();
		roomSave.setHost(hostCheck);
		roomSave.setProvince(provinceCheck);
		roomSave.setBedTotal(room.getBedTotal());
		roomSave.setAreaRoom(room.getAreaRoom());
	    roomSave.setDescription(room.getDescription());
	    roomSave.setName(room.getName());
	    roomSave.setPrice(room.getPrice());
	    roomSave.setMaxDateAmount(room.getMaxDateAmount());
	    roomSave.setGuidelineCheckin(room.getGuidelineCheckin());
	    roomSave.setGuidelineUsingService(room.getGuidelineUsingService());
	    roomSave.setStreetDetail(room.getStreetDetail());
	    roomSave.setStatus("write");
	    roomSave.setRoomType(roomTypeCheck);
		Room roomSuccess = roomRepo.save(roomSave);
		
		room.setId(roomSuccess.getId());
		
		result.put("room", room);
		reponse.setMessage("200");
		reponse.setData(result);
		

		return ResponseEntity.ok(reponse);
	}

	@PostMapping("{room_id}/uploadFile/")
	public ResponseEntity<DataTable> uploadFile(@RequestParam("file") MultipartFile file,
			@PathVariable("room_id") Long room_id) {

		DataTable reponse = new DataTable();
		Map result = new HashMap();

		Room room = roomRepo.findById(room_id).orElse(null);

		if (room == null) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin id không đúng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		String fileName = fileStorageService.storeFile(file);
		room.setRoomProfileImage(fileName);
		roomRepo.save(room);
		reponse.setMessage("200");
		result.put("infor", room);
		reponse.setData(result);

		return ResponseEntity.ok(reponse);

	}

	@PostMapping("{room_id}/uploadListImg/")
	public ResponseEntity<DataTable> uploadListImg(@RequestParam("files") MultipartFile[] files,
			@PathVariable("room_id") Long room_id) {
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		List<RoomImg> roomImgList = new ArrayList<RoomImg>();

		Room room = roomRepo.findById(room_id).orElse(null);

		if (room == null) {
			reponse.setMessage("404");
			result.put("error", "Nhập thông tin id không đúng");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		if (files.length < 0) {
			reponse.setMessage("404");
			result.put("error", "Nhập file hình");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		List<MultipartFile> listFiles = Arrays.asList(files);

		listFiles.forEach((file) -> {
			RoomImg roomImg = new RoomImg();
			String fileName = fileStorageService.storeFile(file);
			if (fileName != null) {
				roomImg.setRoom(room);
				roomImg.setUrl(fileName);
				roomImgList.add(roomImg);
			}
		});

		if (roomImgList.isEmpty()) {
			reponse.setMessage("404");
			result.put("error", "Nhập file hình bị lỗi");
			reponse.setData(result);
			return ResponseEntity.ok(reponse);
		}

		roomImgRepo.saveAll(roomImgList);

		reponse.setMessage("200");
		result.put("key", roomImgList);
		reponse.setData(result);
		return ResponseEntity.ok(reponse);
	}

	@PostMapping("/uploadFile")
	public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);

		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
				.path(fileName).toUriString();

		return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
	}

	@PostMapping("/uploadMultipleFiles")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

//	@PostMapping("/add")
//	public ResponseEntity<?> post(@RequestParam){
//		
//	}

}
