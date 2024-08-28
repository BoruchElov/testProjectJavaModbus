package org.example;

import com.ghgande.j2mod.modbus.Modbus;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.ghgande.j2mod.modbus.ModbusException;

public class Main {

    public static void main(String[] args) {
        // Настройки последовательного порта
        SerialParameters params = new SerialParameters();
        params.setPortName("COM7"); // Укажите ваш COM-порт
        params.setBaudRate(115200); // Установите скорость соединения
        params.setDatabits(8);
        params.setParity(AbstractSerialConnection.NO_PARITY); // Параметры четности: None, Even, Odd
        params.setStopbits(AbstractSerialConnection.ONE_STOP_BIT);
        params.setEncoding(Modbus.SERIAL_ENCODING_RTU); // Устанавливаем RTU режим
        params.setEcho(false); // Отключаем режим эхо
        // Создаем экземпляр ModbusSerialMaster
        ModbusSerialMaster modbusMaster = new ModbusSerialMaster(params);
        modbusMaster.setTimeout(1000); // Устанавливаем таймаут соединения
        modbusMaster.setRetries(3); // Количество попыток переподключения


        boolean connected = false;
        try {
            // Подключение к устройству
            modbusMaster.connect();
            System.out.println("Соединение установлено.");
            connected = true;
            // Прочитать 5 регистров начиная с адреса 0
            int slaveId = 5; // Укажите идентификатор устройства
            int startAddress = 0;
            int quantityOfRegisters = 60;
            Register[] registers = new Register[quantityOfRegisters];


            registers = modbusMaster.readMultipleRegisters(slaveId, startAddress, quantityOfRegisters);
            // Вывод значений регистров
            for (int i = 0; i < registers.length; i++) {
                System.out.println("Register " + (startAddress + i) + " = " + registers[i].getValue());
            }
        } catch (ModbusException e) {
            System.out.println("Ошибка при подключении или работе с устройством: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // Закрываем соединение, если оно было установлено
            if (connected) {
                try {
                    modbusMaster.disconnect();
                    System.out.println("Соединение закрыто.");
                } catch (Exception e) {
                    System.out.println("Ошибка при закрытии соединения: " + e.getMessage());
                }
            }
        }
    }
}
