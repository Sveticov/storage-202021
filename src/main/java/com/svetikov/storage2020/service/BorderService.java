package com.svetikov.storage2020.service;

import com.svetikov.storage2020.component.PLCComponent;
import com.svetikov.storage2020.models.BoardBox;
import com.svetikov.storage2020.models.PLCData;
import com.svetikov.storage2020.models.PLCDbData;
import com.svetikov.storage2020.repository.BoardBoxRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import si.trina.moka7.live.PLC;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Service
@Qualifier("board")
public class BorderService implements ModelService<BoardBox, Long> {
    @Value("${board_csl.plc.name}")
    private String namePLCCSL;
    @Value("${board_sl.plc.name}")
    private String namePLCSL;
    @Qualifier("board")
    private BoardBoxRepository boardBoxRepository;
    private final PLCComponent plcComponent;
    private final ModelService<PLCData, Integer> plcDataService;
    private PLC plc;
    private PLCData plcData;
    private List<PLCDbData> plcDbDataINT;
    private List<PLCDbData> plcDbDataBOOL;
    private ExecutorService executorService;


    @Autowired
    public BorderService(BoardBoxRepository boardBoxRepository,
                         PLCComponent plcComponent,
                         @Qualifier("plc") ModelService<PLCData, Integer> plcDataService) {
        this.boardBoxRepository = boardBoxRepository;
        this.plcComponent = plcComponent;
        this.plcDataService = plcDataService;
        executorService = Executors.newFixedThreadPool(3);
    }


    @Override
    public BoardBox saveModel(BoardBox boardBox) {
        return boardBoxRepository.save(boardBox);
    }

    @Override
    public BoardBox getModelByID(Long aLong) {
        return boardBoxRepository.findById(aLong).orElse(null);
    }

    @Override
    public List<BoardBox> getAllModel() {
        return boardBoxRepository.findAll();
    }

    @Override
    public BoardBox deleteModel(Long aLong) {
        BoardBox boardBox = boardBoxRepository.findById(aLong).orElse(null);
        boardBoxRepository.delete(boardBox);
        return boardBox;
    }

    public void setDataBoard_CSL() {

        setDataBoard(namePLCCSL);
        Runnable taskCSL = () -> {
            boolean status5273 = false;
            boolean status5274 = false;
            while (true) {

                try {
                    boolean table5273Busy = plc.getBool(true, plcDbDataBOOL.get(0).getDbByte(), plcDbDataBOOL.get(0).getDbBit());
                    boolean table5274Busy = plc.getBool(true, plcDbDataBOOL.get(1).getDbByte(), plcDbDataBOOL.get(1).getDbBit());
                    boolean table5275Busy = plc.getBool(true, plcDbDataBOOL.get(2).getDbByte(), plcDbDataBOOL.get(2).getDbBit());

                    int board5273Length = plc.getDInt(true, plcDbDataINT.get(0).getDbByte());
                    int board5273Width = plc.getDInt(true, plcDbDataINT.get(1).getDbByte());
                    int board5273Quantity = plc.getDInt(true, plcDbDataINT.get(2).getDbByte());

                    int board5274Length = plc.getDInt(true, plcDbDataINT.get(3).getDbByte());
                    int board5274Width = plc.getDInt(true, plcDbDataINT.get(4).getDbByte());
                    int board5274Quantity = plc.getDInt(true, plcDbDataINT.get(5).getDbByte());


                    if (table5273Busy == true && status5273 != true) {
                        saveModel(new BoardBox(0, 0, board5273Quantity, board5273Length, board5273Width));
                        status5273 = true;
                    }
                    if (table5273Busy == false) status5273 = false;
                    if (table5274Busy == true && status5274 != true) {
                        saveModel(new BoardBox(0, 0, board5274Quantity, board5274Length, board5274Width));
                        status5274 = true;
                    }
                    if (table5274Busy == false) status5274 = false;


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        executorService.execute(taskCSL);
    }

    public void setDataBoardSL() {
        setDataBoard(namePLCSL);
        Runnable taskSL = () -> {
            boolean status5415 = false;
            int x_pos = 180;
            int z_pos = 561;
            int x_offset = 0;
            int z_offset = 0;
            while (true) {
                try {
                    boolean table5415Busy = plc.getBool(true, plcDbDataBOOL.get(0).getDbByte(), plcDbDataBOOL.get(0).getDbBit());
                    if (table5415Busy == false && status5415 != true) {
                        System.out.println("delete");// TODO: 2/28/2020 delete board position 5415
                        for (BoardBox boardBox : getAllModel()) {
                            x_offset = boardBox.getPositionXBox() - x_pos;
                            z_offset = boardBox.getPositionYBox() - z_pos;
                            if (x_offset > 10||x_offset<-10 && z_offset > 10||z_offset<-10) {
                                deleteModel(boardBox.getIdBox());
                            }
                        }
                        status5415 = true;
                    }
                    if (table5415Busy == true) status5415 = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        executorService.execute(taskSL);
    }

    private void setDataBoard(String name) {
        for (Map.Entry<String, PLC> plcEntry : plcComponent.plcMap().entrySet()) {
            if (plcEntry.getKey().equals(name)) {
                plc = plcEntry.getValue();
            }
        }
        plcData = plcDataService.getAllModel().stream()
                .filter(p -> p.getPlcName().equals(name))
                .findFirst()
                .get();
        plcDbDataINT = plcData.getPlcDbData().stream()
                .filter(d -> d.getTypeDB().equals("INT"))
                .sorted(Comparator.comparingInt(PLCDbData::getDbByte))
                .collect(Collectors.toList());
        plcDbDataBOOL = plcData.getPlcDbData().stream()
                .filter(d -> d.getTypeDB().equals("BOOL"))
                .sorted(Comparator.comparingInt(PLCDbData::getDbByte))
                .collect(Collectors.toList());
    }
}
