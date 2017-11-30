import React from 'react';

export default class TableComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        let rows = this.props.tweets.map((row, i) => {
            return(
                <tr key={i} className={ row.sentiment === 0 ? "bg-danger" : "" }>
                    <td>{row.text}</td>
                    <td>{ row.sentiment === 0 ? row.reason : ""}</td>
                </tr>
            )
        });

        return(
                <table className="table table-sm" style={{ margin: "20px" }}>
                    <thead>
                    <tr>
                        <th>Text</th>
                        <th>Keywords</th>
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>
                </table>
        )
    }
}