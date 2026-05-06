import java.net.*;
import java.util.*;

public class DistributedAverageSystem {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java DistributedAverageSystem <port> <number>");
            System.exit(1);
        }

        int port = 0;
        int number = 0;

        try {
            port = Integer.parseInt(args[0]);
            number = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Both <port> and <number> must be integers.");
            System.exit(1);
        }

        DatagramSocket socket = null;
        boolean isMaster = false;

        try {
            socket = new DatagramSocket(port);
            isMaster = true;
        } catch (SocketException e) {
            isMaster = false;
        }

        if (isMaster) {
            masterMode(socket, port, number);
        } else {
            slaveMode(port, number);
        }
    }

    public static void masterMode(DatagramSocket socket, int port, int initialNumber) {
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(Integer.valueOf(initialNumber));

        System.out.println("Master mode started on port " + port);

        while (true) {
            try {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength()).trim();
                int receivedNumber = Integer.parseInt(received);

                if (receivedNumber != 0 && receivedNumber != -1) {
                    System.out.println(receivedNumber);
                    numbers.add(Integer.valueOf(receivedNumber));
                } else if (receivedNumber == 0) {
                    int sum = 0;
                    int count = 0;
                    for (int num : numbers) {
                        if (num != 0) {
                            sum += num;
                            count++;
                        }
                    }
                    if (count == 0) {
                        System.out.println("Cannot compute average of zero numbers.");
                    } else {
                        int average = (int) Math.round((double) sum / count);
                        System.out.println(average);
                        broadcast(socket, port, average);
                    }
                } else if (receivedNumber == -1) {
                    System.out.println(-1);
                    broadcast(socket, port, -1);
                    socket.close();
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("Received invalid number format.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void slaveMode(int port, int number) {
        try {
            DatagramSocket socket = new DatagramSocket();
            byte[] data = Integer.toString(number).getBytes();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(DatagramSocket socket, int port, int number) {
        try {
            String message = Integer.toString(number);
            byte[] data = message.getBytes();
            socket.setBroadcast(true);
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("255.255.255.255"), port);
            socket.send(packet);
            System.out.println("Broadcasted: " + number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}