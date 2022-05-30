package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private static SellerDao SellerDao = DaoFactory.createSellerDao();
	
	public static List<Seller> findAll(){
		return SellerDao.findAll();
	}
	
	public static void saveOrUpdate(Seller Seller) {
		if(Seller.getId()==null) {
			SellerDao.insert(Seller);
		} else {
			SellerDao.update(Seller);
		}
	}

	public static void remove(Seller Seller) {
		SellerDao.delete(Seller.getId());
	}
}
;