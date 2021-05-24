package booking.home.booking.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import antlr.collections.List;
import booking.home.booking.enity.OrderDeal;
import booking.home.booking.repo.OrderDealRepo;
import booking.home.booking.vo.common.DataTable;

@RestController
@RequestMapping("province")
public class ProvinceController {
	
	
	@Autowired
	OrderDealRepo orderRepo;
	
	@PostMapping("/order")
	public ResponseEntity<DataTable> testOrder (OrderDeal order){
		DataTable reponse = new DataTable();
		Map result = new HashMap();
		
		orderRepo.save(order);
	
		reponse.setData(order);
		
		return ResponseEntity.ok(reponse);
	}
	
	
//	@GetMapping("/order")
//	public ResponseEntity<DataTable> testOrder (){
//		DataTable reponse = new DataTable();
//		Map result = new HashMap();
//		
//		List<Order> orderList =  orderRepo.findAll();
//	
//		reponse.setData(orderList);
//		
//		return ResponseEntity.ok(reponse);
//	}
}
