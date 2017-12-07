package nassaty.playmatedesign.ui.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nassaty.playmatedesign.R;
import nassaty.playmatedesign.ui.adapters.CreditCardAdapter;
import nassaty.playmatedesign.ui.adapters.FriendAdapter;
import nassaty.playmatedesign.ui.helpers.Constants;
import nassaty.playmatedesign.ui.model.CreditCard;
import nassaty.playmatedesign.ui.utils.DialogHelper;
import nassaty.playmatedesign.ui.utils.FirebaseAgent;
import nassaty.playmatedesign.ui.utils.ItemClickListener;
import nassaty.playmatedesign.ui.utils.ItemDecorator;

public class ViewCreditCards extends AppCompatActivity {

    final int GET_NEW_CARD = 2;

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.cards)
    RecyclerView card_list;
    @BindView(R.id.add_card)
    FloatingActionButton addCard;
    @BindView(R.id.scan_card)
    FloatingActionButton scanCard;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAgent agent;
    private ProgressDialog dialog;
    private CreditCardAdapter adapter;
    private FriendAdapter friendAdapter;
    private FirebaseDatabase database;
    private DatabaseReference credit_cards;
    private DatabaseReference users;
    private List<CreditCard> creditCards;
    private DialogHelper dialogHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credit_card);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Cards");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        agent = new FirebaseAgent(this);
        dialogHelper = new DialogHelper(this);
        database = FirebaseDatabase.getInstance();
        credit_cards = database.getReference().child(Constants.DATABASE_PATH_CREDIT_CARDS);
        users = database.getReference().child(Constants.DATABASE_PATH_USERS).child(auth.getCurrentUser().getPhoneNumber());


        if (user != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            card_list.setLayoutManager(layoutManager);
            card_list.addItemDecoration(new ItemDecorator(this, ItemDecorator.VERTICAL_LIST, 2));
            creditCards = new ArrayList<>();
            fetchSavedCard();
        }

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCard();
            }
        });
        scanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void addCard() {
        Intent intent = new Intent(ViewCreditCards.this, AddCreditCard.class);
        startActivityForResult(intent, GET_NEW_CARD);
    }

    public void fetchSavedCard() {
        users.child(Constants.DATABASE_PATH_CREDIT_CARDS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, final String s) {
                CreditCard card = dataSnapshot.getValue(CreditCard.class);
                creditCards.add(card);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                adapter.notifyDataSetChanged();
            }
        });
        adapter = new CreditCardAdapter(creditCards, ViewCreditCards.this);
        card_list.setAdapter(adapter);

        card_list
                .addOnItemTouchListener(new ItemClickListener(
                        ViewCreditCards.this, new ItemClickListener.OnItemClickListener() {
            @Override
            public void OnItemClicked(View view, int position) {
                dialogHelper.showSimpleDialog("PayBack Process", "Card chosen successfully");
            }
        }));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_NEW_CARD && resultCode == RESULT_OK) {



        }

    }

    public void MessageDialog(String msg) {
        LovelyInfoDialog dialog = new LovelyInfoDialog(this);
        dialog.setMessage(msg);
        dialog.show();
    }

}
