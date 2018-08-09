package findlocation;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.swing.MapView;
import com.teamdev.jxmaps.*;
import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.DirectionsRequest;
import com.teamdev.jxmaps.DirectionsResult;
import com.teamdev.jxmaps.DirectionsRouteCallback;
import com.teamdev.jxmaps.DirectionsStatus;
import com.teamdev.jxmaps.TravelMode;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author Will Harrill
 */
public class Maps extends MapView 
{
    private Map map;
    private String strFrom;
    private String strTo;
    private static final String Initial_from = "Green Bay, WI";
    private static final String Initial_to = "Orlando, FL";
    JTextField fromField = new JTextField();
    JTextField toField = new JTextField();
    
    
    public Maps(String Name)
    {
        JFrame frame = new JFrame(Name);
        
        setOnMapReadyHandler(new MapReadyHandler()
        {
            @Override
            public void onMapReady(MapStatus Status)
            {
                if(Status == MapStatus.MAP_STATUS_OK)
                {
                    map = getMap();
                    //map.setCenter(new LatLng(map, 41.85, -87.65));
                    MapOptions mapOptions = new MapOptions();
                    MapTypeControlOptions controlOptions = new MapTypeControlOptions();
                    mapOptions.setMapTypeControlOptions(controlOptions);
                    
                    map.setOptions(mapOptions);
                        calculateDirection(Initial_from,Initial_to);
                    map.setZoom(6.0);
                    
                    //Marker mark = new Marker(map);
                    //mark.setPosition(map.getCenter());
                }
            }
        });

        final JButton calcBtn = new JButton("Calculate");
        final JTextField toField = new JTextField("To:",15);
        final JTextField fromField = new JTextField("From:",15);
        ActionListener listener = new ActionListener() {
   
        @Override
            public void actionPerformed(ActionEvent e) 
            {
                String from = fromField.getText();
                String to = toField.getText();
                fromField.setText(from);
                toField.setText(to);
                calculateDirection(fromField.getText(),toField.getText());
            }
        };
        calcBtn.addActionListener(listener);
        fromField.addActionListener(listener);
        toField.addActionListener(listener);
        
        JPanel toolBar = new JPanel();
        toolBar.add(calcBtn);
        toolBar.add(fromField);
        toolBar.add(toField);
        
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this, BorderLayout.CENTER);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.setSize(700, 500);
        frame.setVisible(true);
        
    }
    private void calculateDirection(String from, String to) {
        // Getting the associated map object
        final Map map = getMap();
        // Creating a directions request
        DirectionsRequest request = new DirectionsRequest(map);     
        // Setting of the origin location to the request
        request.setOriginString(from);
        // Setting of the destination location to the request
        request.setDestinationString(to);
        // Setting of the travel mode
        request.setTravelMode(TravelMode.DRIVING);
        // Calculating the route between locations
        getServices().getDirectionService().route(request, new DirectionsRouteCallback(map) {
            @Override
            public void onRoute(DirectionsResult result, DirectionsStatus status) {
                // Checking of the operation status
                if (status == DirectionsStatus.OK) {
                    // Drawing the calculated route on the map
                    map.getDirectionsRenderer().setDirections(result);
                } else {
                    JOptionPane.showMessageDialog(Maps.this, "Error. Route cannot be calculated.\nPlease correct input data.");     
                }
            }
        });
    }
    public static void main(String[] args) { Maps map = new Maps("Directions Beta"); }   
}